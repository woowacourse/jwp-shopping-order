package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.CartItemDao;
import cart.domain.Member;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import cart.service.request.OrderRequestDto;
import cart.service.response.OrderResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

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
//        order.checkOwner(member);
        return OrderResponseDto.from(order);
    }

    public Long order(final Member member, final OrderRequestDto requestDto) {
        final List<CartItem> cartItems = requestDto.getCartItemIds().stream()
                .map(cartItemDao::findById)
                .collect(toUnmodifiableList());
        //주문하기
        final Order order = Order.of(member, cartItems);

        final Order savedOrder = orderRepository.save(order);
        for (final CartItem cartItem : cartItems) {
            cartItemDao.delete(cartItem.getMemberId(), cartItem.getProduct().getId());
        }
        order.getMemberCoupon().ifPresent(memberCouponRepository::useMemberCoupon);

        return savedOrder.getId();
    }
}
