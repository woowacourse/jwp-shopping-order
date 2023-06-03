package cart.application;

import static cart.application.mapper.OrderMapper.converBasicOrder;
import static cart.application.mapper.OrderMapper.convertCouponOrder;
import static cart.application.mapper.OrderMapper.convertOrderResponse;
import static cart.persistence.mapper.CartMapper.convertCartItemWithId;

import cart.application.dto.order.OrderProductRequest;
import cart.application.dto.order.OrderRefundResponse;
import cart.application.dto.order.OrderRequest;
import cart.application.dto.order.OrderResponse;
import cart.application.mapper.OrderMapper;
import cart.domain.cartitem.Cart;
import cart.domain.cartitem.CartItemWithId;
import cart.domain.cartitem.CartRepository;
import cart.domain.coupon.CouponWithId;
import cart.domain.event.FirstOrderCouponEvent;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.domain.member.MemberRepository;
import cart.domain.member.MemberWithId;
import cart.domain.order.BasicOrder;
import cart.domain.order.BigDecimalConverter;
import cart.domain.order.CouponOrder;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.order.OrderWithId;
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
        final List<CartItemWithId> cartItems = cart.getCartItems();
        final List<Long> requestProductIds = getRequestProductIds(orderRequest);
        validateOrderProductIds(requestProductIds, cartItems);

        final MemberWithId member = memberRepository.findWithIdByName(memberName);
        final List<CartItemWithId> requestCartItems = getOrderRequestCartItems(orderRequest, cartItems);
        validateDeletedProductExistence(requestCartItems);

        final long savedOrderId = saveOrder(requestCartItems, member, orderRequest.getCouponId());
        cartRepository.deleteByProductIdsAndMemberName(requestProductIds, memberName);
        applicationEventPublisher.publishEvent(new FirstOrderCouponEvent(member.getMemberId()));
        return savedOrderId;
    }

    public OrderResponse getOrderById(final String memberName, final Long id) {
        final OrderWithId orderWithId = orderRepository.getById(id);
        if (orderWithId.isNotOwner(memberName)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        return convertOrderResponse(orderWithId);
    }

    public List<OrderResponse> getOrders(final String memberName) {
        final List<OrderWithId> ordersWithId = orderRepository.findByMemberName(memberName);
        return ordersWithId.stream()
            .map(OrderMapper::convertOrderResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderRefundResponse cancelOrder(final String memberName, final Long id) {
        final OrderWithId orderWithId = orderRepository.getById(id);
        if (orderWithId.isNotOwner(memberName)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        final Order order = orderWithId.getOrder();
        final LocalDateTime currentTime = LocalDateTime.now();
        final RefundPolicy refundPolicy = refundPolicyComposite.getRefundPolicies(order, currentTime);
        orderRepository.updateNotValidById(orderWithId.getOrderId());

        final BigDecimal paymentPrice = changeCouponUnUsedIfExist(order);
        final BigDecimal refundPrice = refundPolicy.calculatePrice(paymentPrice).add(
            BigDecimalConverter.convert(order.getDeliveryPrice()));
        return new OrderRefundResponse(refundPrice);
    }

    private BigDecimal changeCouponUnUsedIfExist(final Order order) {
        if (order.getCoupon().isEmpty()) {
            return order.getTotalPrice();
        }
        final CouponWithId coupon = order.getCoupon().get();
        final Long memberId = order.getMember().getMemberId();
        if (coupon.isNotExpired(LocalDateTime.now())) {
            memberCouponRepository.updateNotUsed(memberId, coupon.getCouponId());
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

    private void validateOrderProductIds(final List<Long> requestProductIds, final List<CartItemWithId> cartItems) {
        final List<Long> productIds = getExistProductIds(cartItems);
        if (!new HashSet<>(productIds).containsAll(requestProductIds)) {
            throw new BadRequestException(ErrorCode.ORDER_INVALID_PRODUCTS);
        }
    }

    private List<Long> getExistProductIds(final List<CartItemWithId> cartItems) {
        return cartItems.stream()
            .map(cartItemWithId -> cartItemWithId.getProduct().getProductId())
            .collect(Collectors.toUnmodifiableList());
    }

    private List<CartItemWithId> getOrderRequestCartItems(final OrderRequest orderRequest,
                                                          final List<CartItemWithId> cartItems) {
        return cartItems.stream()
            .flatMap(cartItemWithId -> orderRequest.getItems().stream()
                .filter(orderProductRequest -> cartItemWithId.isSameProductId(orderProductRequest.getProductId()))
                .map(orderProductRequest -> convertCartItemWithId(cartItemWithId, orderProductRequest)))
            .collect(Collectors.toList());
    }

    private void validateDeletedProductExistence(final List<CartItemWithId> requestCartItems) {
        final long deletedProductCount = requestCartItems.stream()
            .filter(CartItemWithId::isDeleted)
            .count();
        if (deletedProductCount > 0) {
            throw new BadRequestException(ErrorCode.PRODUCT_DELETED);
        }
    }

    private long saveOrder(final List<CartItemWithId> productWithIds,
                           final MemberWithId memberWithId, final Long couponId) {
        if (couponId == null) {
            return saveBasicOrder(productWithIds, memberWithId);
        }
        return saveCouponOrder(productWithIds, memberWithId, couponId);
    }

    private long saveBasicOrder(final List<CartItemWithId> productWithIds, final MemberWithId memberWithId) {
        final BasicOrder order = converBasicOrder(productWithIds, DELIVERY_PRICE, memberWithId);
        return orderRepository.save(order);
    }

    private long saveCouponOrder(final List<CartItemWithId> productWithIds,
                                 final MemberWithId memberWithId, final Long couponId) {
        final Long memberId = memberWithId.getMemberId();
        final MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(memberId, couponId);
        memberCoupon.checkValid();
        final CouponOrder order = convertCouponOrder(productWithIds, DELIVERY_PRICE, memberWithId, memberCoupon);
        final Long savedOrderId = orderRepository.saveWithCoupon(order);
        memberCouponRepository.updateUsed(memberId, couponId);
        return savedOrderId;
    }
}
