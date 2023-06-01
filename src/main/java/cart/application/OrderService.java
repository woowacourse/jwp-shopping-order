package cart.application;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderHistory;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Payment;
import cart.dto.order.OrderDetailResponse;
import cart.dto.order.OrderProductsRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository, CartItemService cartItemService, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemService = cartItemService;
        this.memberRepository = memberRepository;
    }

    public long orderProducts(Member member, OrderProductsRequest orderProductsRequest) {
        List<CartItem> cartItems = toCartItems(orderProductsRequest.getCartItemIds());
        OrderItems orderItems = toOrderItems(cartItems);
        Payment payment = new Payment(orderItems.calculateTotalPayment(), orderProductsRequest.getUsedPoint());
        Order order = new Order(member, orderItems, payment);
        // 장바구니에서 삭제
        cartItems.forEach(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
        // 주문 목록에 저장
        long orderId = orderRepository.createOrder(order);
        // 포인트 적립
        member.usePoint(payment.getUsedPoint());
        member.earnPoint(payment);

        // 포인트 정보 저장
        memberRepository.updatePoint(member.getId(), member.getPoint());
        return orderId;
    }

    private List<CartItem> toCartItems(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartItemRepository::findCartItemById)
                .collect(Collectors.toList());
    }

    private OrderItems toOrderItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toOrderItem)
                .collect(collectingAndThen(toList(), OrderItems::new));
    }

    private OrderItem toOrderItem(CartItem cartItem) {
        return new OrderItem.Builder()
                .id(cartItem.getId())
                .name(cartItem.getProduct().getName())
                .price(cartItem.getProduct().getPrice())
                .imageUrl(cartItem.getProduct().getImageUrl())
                .quantity(cartItem.getQuantity())
                .totalPayment(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                .build();
    }

    public List<OrderHistory> getOrderItems(Member member) {
        System.out.println("\n!!!! OrderService.getOrderItems() 반환값 변경 필요\n");
        return null;
    }

    public List<OrderDetailResponse> getOrderItemDetailById(Member member, long id) {
        System.out.println("\n!!!! OrderService.getOrderItemDetailById() 반환값 변경 필요\n");
        return null;
    }
}
