package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.ui.dto.OrderCreateRequest;
import cart.ui.dto.OrderItemDto;
import cart.ui.dto.OrderShowResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final CartItemRepository cartItemRepository,
            final MemberRepository memberRepository
    ) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long createOrder(final Member member, final OrderCreateRequest request) {
        final List<OrderItem> orderItems = new ArrayList<>();

        final List<CartItem> cartItems = getCartItems(request);
        for (final CartItem cartItem : cartItems) {
            final OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem);
            orderItems.add(orderItem);

            cartItemRepository.deleteById(cartItem.getId());
        }

        final Order order = new Order(orderItems, request.getShippingFee(), member);
        final Long savedId = orderRepository.saveOrder(order).getId();

        updateMemberInfo(member, order);
        return savedId;
    }

    private List<CartItem> getCartItems(final OrderCreateRequest request) {
        return request.getCartItemIds()
                .stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
    }

    private void updateMemberInfo(Member member, Order order) {
        member.addTotalPurchaseAmount(order.calculateTotalPrice());
        member.upgradeGrade();
        memberRepository.update(member);
    }

    public OrderShowResponse showOrder(final Member member, final Long orderId) {
        final List<OrderItem> orderItems = orderRepository.findAllOrderItemsByIdAndMemberId(orderId, member.getId());
        final List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
        final Order order = orderRepository.findOrderById(member, orderId, orderItems);
        return OrderShowResponse.of(orderItemDtos, order);
    }

    public List<OrderShowResponse> showAllOrders(final Member member) {
        final List<OrderShowResponse> orderShowResponses = new ArrayList<>();

        final List<Order> orders = orderRepository.findAllByMember(member);
        for (final Order order : orders) {
            final List<OrderItemDto> orderItemDtos = getOrderItemDtos(member, order);
            orderShowResponses.add(OrderShowResponse.of(orderItemDtos, order));
        }
        return orderShowResponses;
    }

    private List<OrderItemDto> getOrderItemDtos(final Member member, final Order order) {
        return orderRepository.findAllOrderItemsByIdAndMemberId(order.getId(),
                        member.getId())
                .stream()
                .map(OrderItemDto::from)
                .collect(Collectors.toList());
    }
}
