package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cart.application.dto.CartItemQuantityUpdateRequest;
import cart.application.dto.CartItemRequest;
import cart.application.dto.CartItemResponse;
import cart.application.event.CartItemDeleteEvent;
import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.QuantityAndProduct;

@Service
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemResponse> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemResponse::of).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        return cartItemDao.save(new CartItem(member, productDao.getProductById(cartItemRequest.getProductId())));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void removeAllWithOutCheckingOwner(CartItemDeleteEvent event) {
        long memberId = event.getMemberId();
        List<QuantityAndProduct> quantityAndProducts = event.getQuantityAndProducts();
        for (QuantityAndProduct quantityAndProduct : quantityAndProducts) {
            Product product = quantityAndProduct.getProduct();
            cartItemDao.delete(memberId, product.getId());
        }
    }
}
