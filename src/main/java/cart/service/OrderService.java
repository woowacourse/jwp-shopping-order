package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.IncorrectPriceException;
import cart.exception.NonExistCartItemException;
import cart.exception.NonExistCouponException;
import cart.exception.NonExistOrderException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CouponService couponService;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository,
                        MemberCouponRepository memberCouponRepository, CouponService couponService) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.couponService = couponService;
    }

    public Long register(OrderRequest orderRequest, Member member) {
        List<CartItem> cartItems = orderRequest.getCartItemIds().stream()
                .map(this::getCartItem)
                .collect(Collectors.toList());

        MemberCoupon memberCoupon = getMemberCoupon(orderRequest.getCouponId(), member);

        Order order = Order.of(member, cartItems, orderRequest.getDeliveryFee(), memberCoupon);

        Money totalPrice = order.calculateTotalPrice();
        if (totalPrice.isNotSameValue(orderRequest.getTotalOrderPrice())) {
            throw new IncorrectPriceException();
        }

        Order savedOrder = orderRepository.save(order);
        deleteOrdered(cartItems, memberCoupon);
        if (order.isUnusedCoupon()) {
            couponService.issueByOrderPrice(totalPrice, member);
        }
        return savedOrder.getId();
    }

    private MemberCoupon getMemberCoupon(Long couponId, Member member) {
        if (couponId == -1L) {
            return new MemberCoupon(member, Coupon.NONE);
        }
        return memberCouponRepository.findById(couponId)
                .orElseThrow(NonExistCouponException::new);
    }

    private void deleteOrdered(List<CartItem> cartItems, MemberCoupon memberCoupon) {
        cartItems.stream()
                .map(CartItem::getId)
                .forEach(cartItemRepository::deleteById);
        if (memberCoupon.isExists()) {
            memberCouponRepository.delete(memberCoupon);
        }
    }

    private CartItem getCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(NonExistCartItemException::new);
    }

    public OrderDetailResponse findById(Long id, Member member) {
        Order order = orderRepository.findById(id)
                .orElseThrow(NonExistOrderException::new);
        order.checkOwner(member);
        return OrderDetailResponse.from(order);
    }

    public List<OrderResponse> findAll(Member member) {
        List<Order> orders = orderRepository.findAllByMember(member);

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }
}
