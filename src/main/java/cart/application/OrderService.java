package cart.application;

import static cart.application.mapper.OrderMapper.converBasicOrder;
import static cart.application.mapper.OrderMapper.convertCouponOrder;
import static cart.application.mapper.OrderMapper.convertOrderResponse;
import static cart.persistence.mapper.CartMapper.convertCartItem;

import cart.application.dto.order.OrderProductRequest;
import cart.application.dto.order.OrderRefundResponse;
import cart.application.dto.order.OrderRequest;
import cart.application.dto.order.OrderResponse;
import cart.application.mapper.OrderMapper;
import cart.domain.cartitem.Cart;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartRepository;
import cart.domain.coupon.Coupon;
import cart.domain.event.FirstOrderCouponEvent;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.domain.member.MemberRepository;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.refund.RefundPolicy;
import cart.domain.refund.RefundPolicyComposite;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import cart.exception.ForbiddenException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final int MAX_ORDER_QUANTITY = 100_000;
    private static final int DELIVERY_PRICE = 3_000;

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final RefundPolicyComposite refundPolicyComposite;
    private final MemberCouponRepository memberCouponRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(final CartRepository cartRepository, final OrderRepository orderRepository,
                        final MemberRepository memberRepository, final RefundPolicyComposite refundPolicyComposite,
                        final MemberCouponRepository memberCouponRepository,
                        final ApplicationEventPublisher applicationEventPublisher) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.refundPolicyComposite = refundPolicyComposite;
        this.memberCouponRepository = memberCouponRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public Long orderProduct(final String memberName, final OrderRequest orderRequest) {
        validateOrderQuantity(orderRequest);
        final Cart cart = cartRepository.findByMemberName(memberName);
        final List<CartItem> cartItems = cart.getCartItems();
        final List<Long> requestProductIds = getRequestProductIds(orderRequest);
        validateOrderProductIds(requestProductIds, cartItems);

        final Member member = memberRepository.findByName(memberName);
        final List<CartItem> requestCartItems = getOrderRequestCartItems(orderRequest, cartItems);
        validateDeletedProductExistence(requestCartItems);

        final long savedOrderId = saveOrder(requestCartItems, member, orderRequest.getCouponId());
        cartRepository.deleteByProductIdsAndMemberId(requestProductIds, member.memberId());
        applicationEventPublisher.publishEvent(new FirstOrderCouponEvent(member.memberId()));
        return savedOrderId;
    }

    public OrderResponse getOrderById(final String memberName, final Long id) {
        final Order Order = orderRepository.getById(id);
        if (Order.isNotOwner(memberName)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        return convertOrderResponse(Order);
    }

    public List<OrderResponse> getOrders(final String memberName) {
        final List<Order> orders = orderRepository.findByMemberName(memberName);
        return orders.stream()
            .map(OrderMapper::convertOrderResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderRefundResponse cancelOrder(final String memberName, final Long id) {
        final Order order = orderRepository.getById(id);
        if (order.isNotOwner(memberName)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        final LocalDateTime currentTime = LocalDateTime.now();
        final RefundPolicy refundPolicy = refundPolicyComposite.getRefundPolicies(order, currentTime);
        orderRepository.updateNotValidById(order.getOrderId());

        final BigDecimal paymentPrice = changeCouponUnUsedIfExist(order);
        final BigDecimal refundPrice = refundPolicy.calculatePrice(paymentPrice).add(
            BigDecimal.valueOf(order.getDeliveryPrice()));
        return new OrderRefundResponse(refundPrice);
    }

    private BigDecimal changeCouponUnUsedIfExist(final Order order) {
        if (order.getCoupon().isEmpty()) {
            return order.getTotalPrice();
        }
        final Coupon coupon = order.getCoupon().get();
        final Long memberId = order.getMember().memberId();
        if (coupon.isNotExpired(LocalDateTime.now())) {
            memberCouponRepository.updateNotUsed(memberId, coupon.couponId());
            return order.getDiscountedTotalPrice();
        }
        return order.getTotalPrice();
    }

    private void validateOrderQuantity(final OrderRequest orderRequest) {
        final int totalQuantity = orderRequest.getItems().stream()
            .mapToInt(OrderProductRequest::getQuantity)
            .sum();
        if (totalQuantity > MAX_ORDER_QUANTITY) {
            throw new BadRequestException(ErrorCode.ORDER_QUANTITY_EXCEED);
        }
    }

    private List<Long> getRequestProductIds(final OrderRequest orderRequest) {
        return orderRequest.getItems().stream()
            .map(OrderProductRequest::getProductId)
            .collect(Collectors.toUnmodifiableList());
    }

    private void validateOrderProductIds(final List<Long> requestProductIds, final List<CartItem> cartItems) {
        final List<Long> productIds = getExistProductIds(cartItems);
        if (!new HashSet<>(productIds).containsAll(requestProductIds)) {
            throw new BadRequestException(ErrorCode.ORDER_INVALID_PRODUCTS);
        }
    }

    private List<Long> getExistProductIds(final List<CartItem> cartItems) {
        return cartItems.stream()
            .map(cartItem -> cartItem.getProduct().getProductId())
            .collect(Collectors.toUnmodifiableList());
    }

    private List<CartItem> getOrderRequestCartItems(final OrderRequest orderRequest,
                                                    final List<CartItem> cartItems) {
        return cartItems.stream()
            .flatMap(cartItem -> orderRequest.getItems().stream()
                .filter(orderProductRequest -> cartItem.isSameProductId(orderProductRequest.getProductId()))
                .map(orderProductRequest -> convertCartItem(cartItem, orderProductRequest)))
            .collect(Collectors.toList());
    }

    private void validateDeletedProductExistence(final List<CartItem> requestCartItems) {
        final long deletedProductCount = requestCartItems.stream()
            .filter(CartItem::isDeleted)
            .count();
        if (deletedProductCount > 0) {
            throw new BadRequestException(ErrorCode.PRODUCT_DELETED);
        }
    }

    private long saveOrder(final List<CartItem> cartItems,
                           final Member Member, final Long couponId) {
        if (couponId == null) {
            return saveBasicOrder(cartItems, Member);
        }
        return saveCouponOrder(cartItems, Member, couponId);
    }

    private long saveBasicOrder(final List<CartItem> cartItems, final Member Member) {
        final BasicOrder order = converBasicOrder(cartItems, DELIVERY_PRICE, Member);
        return orderRepository.save(order);
    }

    private long saveCouponOrder(final List<CartItem> cartItems,
                                 final Member Member, final Long couponId) {
        final Long memberId = Member.memberId();
        final MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(memberId, couponId);
        memberCoupon.checkValid();
        final CouponOrder order = convertCouponOrder(cartItems, DELIVERY_PRICE, Member, memberCoupon);
        final Long savedOrderId = orderRepository.saveWithCoupon(order);
        memberCouponRepository.updateUsed(memberId, couponId);
        return savedOrderId;
    }
}
