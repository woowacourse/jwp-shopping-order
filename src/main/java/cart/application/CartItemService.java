package cart.application;

import cart.dao.*;
import cart.domain.*;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.request.PaymentRequest;
import cart.dto.response.CartItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final OrderHistoryDao orderHistoryDao;
    private final OrderProductDao orderProductDao;
    private final MemberDao memberDao;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao, final OrderHistoryDao orderHistoryDao, final OrderProductDao orderProductDao, final MemberDao memberDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.orderHistoryDao = orderHistoryDao;
        this.orderProductDao = orderProductDao;
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> findByMember(final Member member) {
        return cartItemDao.findByMemberId(member.getId()).stream()
                .map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        final Product product = productDao.getProductById(cartItemRequest.getProductId());
        final CartItem cartItem = new CartItem(member, product);
        return cartItemDao.save(cartItem);
    }

    @Transactional(readOnly = true)
    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    @Transactional(readOnly = true)
    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    @Transactional
    public Long payment(final Member member, final PaymentRequest request) {
        final List<Long> cartIds = extractCartIds(request);
        final List<CartItem> cartItems = getCartItems(member, cartIds);
        if (!member.isAbleToUsePoint(request.getPoint())) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        final OrderProducts orderProducts = new OrderProducts(cartItems);
        final Order order = new Order(member, request.getPoint(), orderProducts);
        final Long orderId = orderHistoryDao.createOrder(order);
        orderProductDao.createProducts(orderId, order.getOrderProducts());
        member.usePoint(request.getPoint());
        member.savePoint(order.getSavedPoint());
        memberDao.updatePoint(member);
        return orderId;
    }

    private List<Long> extractCartIds(final PaymentRequest request) {
        return request.getCartItemRequests().stream()
                .map(CartItemRequest::getProductId)
                .collect(Collectors.toList());
    }

    private List<CartItem> getCartItems(final Member member, final List<Long> cartIds) {
        final List<CartItem> cartItems = cartItemDao.findByIds(cartIds);
        cartItems.forEach(cartItem -> cartItem.checkOwner(member));
        return cartItems;
    }
}
