package cart.application;

import static cart.application.mapper.OrderMapper.converBasicOrder;
import static cart.application.mapper.OrderMapper.convertCouponOrder;
import static cart.application.mapper.OrderMapper.convertOrderResponse;

import cart.application.dto.order.OrderProductRequest;
import cart.application.dto.order.OrderRequest;
import cart.application.dto.order.OrderResponse;
import cart.application.mapper.OrderMapper;
import cart.domain.cartitem.Cart;
import cart.domain.cartitem.CartItemWithId;
import cart.domain.cartitem.CartRepository;
import cart.domain.event.FirstOrderCouponEvent;
import cart.domain.member.MemberCoupon;
import cart.domain.member.MemberCouponRepository;
import cart.domain.member.MemberRepository;
import cart.domain.member.dto.MemberWithId;
import cart.domain.order.BasicOrder;
import cart.domain.order.CouponOrder;
import cart.domain.order.OrderRepository;
import cart.domain.order.OrderWithId;
import cart.exception.BadRequestException;
import cart.exception.ErrorCode;
import cart.exception.ForbiddenException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private static final int MAX_ORDER_QUANTITY = 1000;
    private static final int DELIVERY_PRICE = 3000;

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderService(final CartRepository cartRepository, final OrderRepository orderRepository,
                        final MemberRepository memberRepository, final MemberCouponRepository memberCouponRepository,
                        final ApplicationEventPublisher applicationEventPublisher) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
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
        final long savedOrderId = saveOrder(requestCartItems, member, orderRequest.getCouponId());
        cartRepository.deleteByCartItemIdsAndMemberId(requestProductIds, memberName);
        applicationEventPublisher.publishEvent(new FirstOrderCouponEvent(member.getMemberId()));
        return savedOrderId;
    }

    public OrderResponse getOrderById(final String memberName, final Long id) {
        final OrderWithId orderWithId = orderRepository.getById(id);
        if (!orderWithId.isOwner(memberName)) {
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

    private void validateOrderQuantity(final OrderRequest orderRequest) {
        final int totalQuantity = orderRequest.getItems().stream()
            .mapToInt(OrderProductRequest::getQuantity)
            .sum();
        if (totalQuantity > MAX_ORDER_QUANTITY) {
            throw new BadRequestException(ErrorCode.ORDER_QUANTITY_EXCEED);
        }
    }

    private List<Long> getExistProductIds(final List<CartItemWithId> cartItems) {
        return cartItems.stream()
            .map(cartItemWithId -> cartItemWithId.getProduct().getProductId())
            .collect(Collectors.toUnmodifiableList());
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

    private List<CartItemWithId> getOrderRequestCartItems(final OrderRequest orderRequest,
                                                          final List<CartItemWithId> cartItems) {
        return cartItems.stream()
            .flatMap(cartItemWithId -> orderRequest.getItems().stream()
                .filter(orderProductRequest -> Objects.equals(
                    cartItemWithId.getProduct().getProductId(), orderProductRequest.getProductId()))
                .map(orderProductRequest -> new CartItemWithId(cartItemWithId.getCartId(),
                    orderProductRequest.getQuantity(), cartItemWithId.getProduct())))
            .collect(Collectors.toList());
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
        validateCouponExpired(memberCoupon);
        validateAlreadyUsed(memberCoupon);
        final CouponOrder order = convertCouponOrder(productWithIds, DELIVERY_PRICE, memberWithId, memberCoupon);
        final Long savedOrderId = orderRepository.saveWithCoupon(order);
        memberCouponRepository.updateUsed(memberId, couponId);
        return savedOrderId;
    }

    private void validateCouponExpired(final MemberCoupon memberCoupon) {
        final LocalDateTime now = LocalDateTime.now();
        if (memberCoupon.getExpiredAt().isBefore(now)) {
            throw new BadRequestException(ErrorCode.COUPON_EXPIRED);
        }
    }

    private void validateAlreadyUsed(final MemberCoupon memberCoupon) {
        if (memberCoupon.isUsed()) {
            throw new BadRequestException(ErrorCode.COUPON_ALREADY_USED);
        }
    }
}
