package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.OrderCheckout;
import cart.domain.Product;
import cart.domain.pointmanager.DefaultPointManager;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.CheckoutResponse;
import cart.exception.CartItemException;
import cart.exception.ProductException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(final Member member) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(final Member member, final CartItemRequest cartItemRequest) {
        if (cartItemDao.isExistBy(member.getId(), cartItemRequest.getProductId())) {
            throw new CartItemException.AlreadyExist(member, cartItemRequest.getProductId());
        }

        final Product product = productDao.getProductById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ProductException.NotFound(cartItemRequest.getProductId()));
        return cartItemDao.save(new CartItem(member, product));
    }

    public void updateQuantity(final Member member, final Long id, final CartItemQuantityUpdateRequest request) {
        final CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemException.NotFound(id));
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(final Member member, final Long id) {
        final CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemException.NotFound(id));
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    public CheckoutResponse makeCheckout(final Member member, final List<Long> ids) {
        final List<CartItem> checkedCartItems = findSelectedCartItems(member, ids);

        final OrderCheckout orderCheckout = OrderCheckout.of(member.getPoints(), checkedCartItems, new DefaultPointManager());

        return CheckoutResponse.of(orderCheckout);
    }

    public List<CartItem> findSelectedCartItems(final Member member, final List<Long> ids) {
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return ids.stream().map(id -> cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new CartItemException.NotFound(id))).collect(Collectors.toList());
    }
}
