package cart.service;

import cart.controller.request.OrderRequestDto;
import cart.controller.response.OrderResponseDto;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.repository.CouponRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    public OrderService(final OrderRepository orderRepository, final CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    public OrderResponseDto orderCartItems(Member member, OrderRequestDto orderRequestDto) {
        List<CartItem> cartItems = orderRepository.findCartItemById(orderRequestDto.getCartItemIds());
        Optional<Coupon> coupon = orderRequestDto.getCouponId()
                .flatMap(couponRepository::findCouponByMemberCouponId);

        Order order = Order.of(member, coupon, cartItems);
        Order orderAfterSave = orderRepository.save(order);

        orderRepository.deleteCartItems(orderRequestDto.getCartItemIds());

        return OrderResponseDto.from(orderAfterSave);
    }
}
