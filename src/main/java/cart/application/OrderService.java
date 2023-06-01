package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.*;
import cart.dto.CartItemOrderRequest;
import cart.dto.OrderInfoResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    
    public OrderService(final OrderRepository orderRepository, final MemberDao memberDao, final CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
    }
    
    public Long order(final Member member, final OrderRequest orderRequest) {
        final Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        final Set<CartItem> cartItems = getCartItems(orderRequest);
        validateMemberOfCartItem(memberByEmail, cartItems);
        final List<OrderInfo> orderInfos = getOrderInfos(cartItems);
        
        final Order order =
                new Order(memberByEmail, orderInfos, orderRequest.getOriginalPrice(), orderRequest.getUsedPoint(), orderRequest.getPointToAdd());
        order.order();
        return orderRepository.insertOrder(memberByEmail, order);
    }
    
    private Set<CartItem> getCartItems(final OrderRequest orderRequest) {
        return orderRequest.getCartItems().stream()
                .map(CartItemOrderRequest::getCartItemId)
                .map(cartItemDao::findById)
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
}
