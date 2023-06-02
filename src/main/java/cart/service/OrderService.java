package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import cart.service.request.OrderRequestDto;
import cart.service.response.OrderResponseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CartItemDao cartItemDao;

    public OrderService(final OrderRepository orderRepository, final MemberCouponRepository memberCouponRepository,
                        final CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.cartItemDao = cartItemDao;
    }

    public OrderResponseDto findOrderById(final Member member, final Long id) {
        final Order order = orderRepository.findById(id);
        //order.checkOwner(member);
        return OrderResponseDto.from(order);
    }

    @Transactional
    public Long order(final Member member, final OrderRequestDto requestDto) {
        final List<CartItem> cartItems = requestDto.getCartItemIds().stream()
                .map(cartItemDao::findById)
                .collect(toUnmodifiableList());
        final Order order = createOrder(member, requestDto, cartItems);
        return orderRepository.save(order, cartItems).getId();
    }

    private Order createOrder(final Member member, final OrderRequestDto orderRequestDto,
                              final List<CartItem> cartItems) {
        final Optional<Long> couponIdOption = orderRequestDto.getOptionalCouponId();
        if (couponIdOption.isPresent()) {
            final Long couponId = couponIdOption.get();
            final MemberCoupon memberCoupon = memberCouponRepository.findUnUsedCouponById(couponId);
            return Order.of(member, cartItems, memberCoupon);
        }
        return Order.of(member, cartItems);
    }

    public List<OrderResponseDto> findOrders(final Member member) {
        return orderRepository.findOrders(member).stream()
                .map(OrderResponseDto::from)
                .collect(toUnmodifiableList());
    }
}
