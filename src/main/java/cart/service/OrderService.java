package cart.service;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Point;
import cart.domain.PointPolicy;
import cart.domain.member.Member;
import cart.dto.OrderItemDto;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.PaymentDto;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemDao cartItemDao;
    private final PointPolicy pointPolicy;

    public OrderService(final OrderRepository orderRepository, final MemberRepository memberRepository, final CartItemDao cartItemDao, final ProductDao productDao, final PointPolicy pointPolicy) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.cartItemDao = cartItemDao;
        this.pointPolicy = pointPolicy;
    }

    public Long addOrder(final Member member, final OrderRequest orderRequest) {
        final List<CartItem> cartItems = getCartItems(orderRequest.getOrderItems());

        final Order order = generateOrder(member, cartItems, orderRequest.getPayment());
        cartItems.forEach(cartItem -> cartItemDao.deleteById(cartItem.getId()));

        final Point savePoint = pointPolicy.save(order.getPayment());
        final Member newMember = order.calculateMemberPoint(savePoint);
        memberRepository.updateMemberPoint(newMember);

        return orderRepository.addOrder(order);
    }

    private List<CartItem> getCartItems(final List<OrderItemDto> orderItemDtos) {
        return orderItemDtos.stream()
                .map(orderItemDto -> cartItemDao.findById(orderItemDto.getCartItemId()))
                .collect(Collectors.toUnmodifiableList());
    }

    private Order generateOrder(final Member member, final List<CartItem> cartItems, final PaymentDto paymentDto) {
        final List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity()))
                .collect(Collectors.toUnmodifiableList());
        return Order.from(member, paymentDto.getFinalPayment(), paymentDto.getPoint(), orderItems);
    }

    public List<OrderResponse> getAllOrders(final Member member) {
        final List<Order> orders = orderRepository.getAllOrders(member);
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderResponse getOrderById(final Member member, final Long orderId) {
        final Order order = orderRepository.getOrderById(member, orderId);
        return OrderResponse.from(order);
    }
}
