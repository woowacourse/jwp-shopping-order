package cart.application;

import cart.application.domain.OrderInfo;
import cart.application.domain.OrderInfos;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.OrderRepository;
import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.application.domain.Product;
import cart.presentation.dto.response.OrderDto;
import cart.presentation.dto.response.OrderResponse;
import cart.presentation.dto.request.OrderRequest;
import cart.presentation.dto.response.SpecificOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                        MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public long issue(Member member, OrderRequest request) {
        Order order = new Order(null, member, makeOrderInfosFromRequest(member, request),
                request.getOriginalPrice(), request.getUsedPoint(), request.getPointToAdd());
        order.adjustPoint();
        memberRepository.update(order.getMember());
        Order inserted = orderRepository.insert(order);
        cartItemRepository.deleteByMemberId(member.getId());
        return inserted.getId();
    }

    private OrderInfos makeOrderInfosFromRequest(Member member, OrderRequest request) {
        List<CartItem> cartItems = makeCartItemsFromRequest(request);
        cartItems.forEach(cartItem -> cartItem.validateIsOwnedBy(member));
        return new OrderInfos(makeOrderInfosFromCartItems(cartItems));
    }

    private List<CartItem> makeCartItemsFromRequest(OrderRequest request) {
        return request.getCartItemIds().stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());
    }

    private List<OrderInfo> makeOrderInfosFromCartItems(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::makeCartItemToOrderInfo)
                .collect(Collectors.toList());
    }

    private OrderInfo makeCartItemToOrderInfo(CartItem cartItem) {
        Product product = cartItem.getProduct();
        return new OrderInfo(null, product, product.getName(), product.getPrice(),
                product.getImageUrl(), cartItem.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(Member member) {
        List<Order> orders = orderRepository.findByMemberId(member.getId());
        return orders.stream()
                .map(order -> new OrderResponse(order.getId(), mapOrderInfosToOrderDto(order.getOrderInfo())))
                .collect(Collectors.toList());
    }

    private List<OrderDto> mapOrderInfosToOrderDto(OrderInfos orderInfos) {
        return orderInfos.getValues().stream()
                .map(this::makeOrderDto)
                .collect(Collectors.toList());
    }

    private OrderDto makeOrderDto(OrderInfo orderInfo) {
        Product product = orderInfo.getProduct();
        return new OrderDto(product.getId(), product.getPrice(), product.getName(),
                product.getImageUrl(), orderInfo.getQuantity());
    }

    @Transactional(readOnly = true)
    public SpecificOrderResponse getSpecificOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.validateIsIssuedBy(member);
        return new SpecificOrderResponse(order.getId(), mapOrderInfosToOrderDto(order.getOrderInfo()),
                order.getOriginalPrice(), order.getUsedPoint(), order.getPointToAdd());
    }
}
