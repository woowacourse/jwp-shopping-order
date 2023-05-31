package cart.application;

import cart.application.dto.request.CartItemQuantityUpdateRequest;
import cart.application.dto.request.CartItemRequest;
import cart.application.dto.request.PaymentRequest;
import cart.application.dto.response.CartItemResponse;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProducts;
import cart.domain.product.Product;
import cart.persistence.dao.OrderHistoryDao;
import cart.persistence.dao.OrderProductDao;
import cart.persistence.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;

    public CartItemService(final CartItemRepository cartItemRepository, final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao) {
        this.cartItemRepository = cartItemRepository;
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(final Member member) {
        return cartItemRepository.findCartItemsByMemberId(member.getId()).stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Product product = cartItemRepository.getProductById(cartItemRequest.getProductId());
        final CartItem cartItem = new CartItem(member, product);
        return cartItemRepository.saveCartItem(cartItem);
    }

    @Transactional(readOnly = true)
    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemRepository.findCartItemById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemRepository.deleteCartItemById(id);
            return;
        }

        cartItem.updateQuantity(request.getQuantity());
        cartItemRepository.updateCartItemQuantity(cartItem);
    }

    @Transactional(readOnly = true)
    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemRepository.findCartItemById(id);
        cartItem.checkOwner(member);

        cartItemRepository.deleteCartItemById(id);
    }

    @Transactional
    public Long payment(final Member member, final PaymentRequest request) {
        final List<Long> cartIds = extractCartIds(request);
        final List<CartItem> cartItems = getCartItems(member, cartIds);
        member.usePoint(request.getPoint());
        final OrderProducts orderProducts = new OrderProducts(cartItems);
        final Order order = new Order(member, request.getPoint(), orderProducts);
        final Long orderId = orderHistoryDao.createOrder(order);
        member.savePoint(order.getSavedPoint());
        orderProductDao.createProducts(orderId, order.getOrderProducts());
        cartItemRepository.updatePoint(member);
        return orderId;
    }

    private List<Long> extractCartIds(final PaymentRequest request) {
        return request.getCartItemRequests().stream()
                .map(CartItemRequest::getProductId)
                .collect(Collectors.toList());
    }

    private List<CartItem> getCartItems(final Member member, final List<Long> cartIds) {
        final List<CartItem> cartItems = cartItemRepository.getCartItemsByIds(cartIds);
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
        return cartItems;
    }
}
