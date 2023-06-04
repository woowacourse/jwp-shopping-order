package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
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

        final List<CartItem> cartItems = request.getCartItemIds()
                .stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
        for (final CartItem cartItem : cartItems) {
            final Product product = cartItem.getProduct();
            final OrderItem orderItem = new OrderItem(
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    cartItem.getQuantity(),
                    product.getDiscountRate()
            );
            orderItems.add(orderItem);

            cartItemRepository.deleteById(cartItem.getId());
        }

        final Order order = new Order(orderItems, request.getShippingFee(), member);
        final Long savedId = orderRepository.saveOrder(order).getId();

        member.addTotalPurchaseAmount(order.calculateTotalPrice());
        member.upgradeGrade();
        memberRepository.update(member);
        return savedId;
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
            final List<OrderItemDto> orderItemDtos = orderRepository.findAllOrderItemsByIdAndMemberId(order.getId(),
                            member.getId())
                    .stream()
                    .map(OrderItemDto::from)
                    .collect(Collectors.toList());
            orderShowResponses.add(OrderShowResponse.of(orderItemDtos, order));
        }
        return orderShowResponses;
    }
}
