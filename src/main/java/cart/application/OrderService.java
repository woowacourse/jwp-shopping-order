package cart.application;

import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.delivery.DeliveryPolicy;
import cart.domain.discount.DiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.order.OrderPrice;
import cart.domain.respository.cartitem.CartItemRepository;
import cart.domain.respository.member.MemberRepository;
import cart.domain.respository.order.OrderRepository;
import cart.domain.respository.orderitem.OrderItemRepository;
import cart.domain.respository.product.ProductRepository;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.exception.MemberNotExistException;
import cart.exception.OrderException;
import cart.exception.ProductException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final DiscountPolicy discountPolicy;
    private final DeliveryPolicy deliveryPolicy;

    public OrderService(
        final OrderRepository orderRepository,
        final MemberRepository memberRepository,
        final ProductRepository productRepository,
        final CartItemRepository cartItemRepository,
        final OrderItemRepository orderItemRepository,
        final DiscountPolicy discountPolicy,
        final DeliveryPolicy deliveryPolicy
    ) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.discountPolicy = discountPolicy;
        this.deliveryPolicy = deliveryPolicy;
    }

    public OrderResponse createOrder(final Long memberId, final OrderRequest orderRequest) {
        final Member member = findExistMemberById(memberId);
        final OrderItems orderItems = generateOrderItems(orderRequest);
        final CartItems cartItems = new CartItems(cartItemRepository.findByMemberId(memberId));

        orderItems.getItems()
            .forEach(orderItem -> cartItems.buy(orderItem.getProduct().getId(), orderItem.getQuantity()));

        final Order order = cartItems.order(member, orderRequest.getOrderTime(), discountPolicy, deliveryPolicy);
        final Order persistOrder = orderRepository.insert(order, order.getOrderPrice());

        saveOrderItems(persistOrder);
        deleteCartItems(persistOrder);

        return OrderResponse.of(persistOrder,
            OrderPrice.of(persistOrder.getProductPrice(), discountPolicy, deliveryPolicy));
    }

    private Member findExistMemberById(final Long id) {
        return memberRepository.getMemberById(id)
            .orElseThrow(() -> new MemberNotExistException("해당 멤버가 존재하지 않습니다."));
    }

    private OrderItems generateOrderItems(final OrderRequest orderRequest) {
        final List<OrderItem> orderItems = orderRequest.getOrderItems()
            .stream()
            .map((orderItem) -> {
                final Product pesistedProduct = productRepository.getProductById(orderItem.getId())
                    .orElseThrow(() -> new ProductException.ProductNotExistException("상품이 존재하지 않습니다."));
                return OrderItem.notPersisted(pesistedProduct, orderItem.getQuantity());
            })
            .collect(Collectors.toList());

        return new OrderItems(orderItems);
    }

    private void saveOrderItems(final Order order) {
        orderItemRepository.insertAll(order.getId(), order.getOrderItems());
    }

    private void deleteCartItems(final Order order) {
        final List<Long> productIds = order.getOrderItems()
            .stream()
            .map((orderItem -> orderItem.getProduct().getId()))
            .collect(Collectors.toList());
        cartItemRepository.deleteByMemberIdAndProductIds(order.getMemberId(), productIds);
    }

    public OrderResponse getOrderById(final Member member, final Long orderId) {
        final Order persistedOrder = orderRepository.findByOrderId(orderId);
        if (!persistedOrder.getMember().equals(member)) {
            throw new OrderException.IllegalMember("해당 멤버가 한 주문이 아닙니다.");
        }

        return OrderResponse.of(persistedOrder, persistedOrder.getOrderPrice());
    }

    public OrdersResponse getOrderByMemberId(final Long memberId) {
        final List<Order> persistedOrders = orderRepository.findAllByMemberId(memberId);

        final List<OrderResponse> orderResponses = persistedOrders.stream()
            .map(order -> OrderResponse.of(order, order.getOrderPrice()))
            .collect(Collectors.toList());

        return new OrdersResponse(orderResponses);
    }
}
