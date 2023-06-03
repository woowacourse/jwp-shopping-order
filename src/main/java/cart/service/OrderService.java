package cart.service;

import cart.dao.CartItemDao;
import cart.domain.cartItem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.point.Point;
import cart.domain.point.PointPolicy;
import cart.dto.OrderItemDto;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.PageRequest;
import cart.dto.PagingOrderResponse;
import cart.dto.PaymentDto;
import cart.paging.Paging;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemDao cartItemDao;
    private final PointPolicy pointPolicy;

    public OrderService(final OrderRepository orderRepository, final MemberRepository memberRepository, final CartItemDao cartItemDao, final PointPolicy pointPolicy) {
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

    @Transactional(readOnly = true)
    public PagingOrderResponse getAllOrders(final Member member, final PageRequest pageRequest) {
        final Paging paging = new Paging(pageRequest);

        final List<Order> orders = orderRepository.getAllOrders(member, paging.getStart(), paging.getSize());
        final List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());

        final int count = orderRepository.countAllOrders();
        return new PagingOrderResponse(paging.getPageInfo(count), orderResponses);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(final Member member, final Long orderId) {
        final Order order = orderRepository.getOrderById(member, orderId);
        return OrderResponse.from(order);
    }
}
