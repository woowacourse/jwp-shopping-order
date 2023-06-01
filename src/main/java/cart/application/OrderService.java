package cart.application;

import cart.domain.*;
import cart.dto.*;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    
    public OrderService(final OrderRepository orderRepository, final MemberRepository memberRepository, final CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.cartItemRepository = cartItemRepository;
    }
    
    public Long order(final Member member, final OrderRequest orderRequest) {
        final Member memberByEmail = memberRepository.getMemberByEmail(member.getEmail());
        final Set<CartItem> cartItems = getCartItems(orderRequest);
        validateMemberOfCartItem(memberByEmail, cartItems);
        final List<OrderInfo> orderInfos = getOrderInfos(cartItems);
        removeCartItems(orderRequest);
        
        final Order order =
                new Order(memberByEmail, orderInfos, orderRequest.getOriginalPrice(), orderRequest.getUsedPoint(), orderRequest.getPointToAdd());
        order.order();
        return orderRepository.save(memberByEmail, order);
    }
    
    private Set<CartItem> getCartItems(final OrderRequest orderRequest) {
        return orderRequest.getCartItems().stream()
                .map(CartItemOrderRequest::getCartItemId)
                .map(cartItemRepository::findById)
                .collect(Collectors.toUnmodifiableSet());
    }
    
    private void validateMemberOfCartItem(final Member member, final Set<CartItem> cartItems) {
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
    }
    
    private List<OrderInfo> getOrderInfos(final Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> {
                    final Product product = cartItem.getProduct();
                    return new OrderInfo(product, product.getName(), product.getPrice(), product.getImageUrl(), cartItem.getQuantity());
                })
                .collect(Collectors.toUnmodifiableList());
    }
    
    private void removeCartItems(final OrderRequest orderRequest) {
        orderRequest.getCartItems().stream()
                .map(CartItemOrderRequest::getCartItemId)
                .forEach(cartItemRepository::removeById);
    }
    
    @Transactional(readOnly = true)
    public List<OrderResponse> findByMember(final Member member) {
        return orderRepository.findByMember(member).stream()
                .map(order -> new OrderResponse(order.getId(), getOrderInfoResponses(order)))
                .collect(Collectors.toUnmodifiableList());
    }
    
    private List<OrderInfoResponse> getOrderInfoResponses(final Order order) {
        return order.getOrderInfos().getOrderInfos().stream()
                .map(OrderInfoResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
    
    @Transactional(readOnly = true)
    public OrderDetailResponse findByMemberAndId(final Member member, final Long orderId) {
        final Order order = orderRepository.findByMemberAndId(member, orderId);
        return OrderDetailResponse.from(order);
    }
}
