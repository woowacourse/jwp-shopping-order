package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemDto;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.exception.notFound.CartItemNotFountException;
import cart.exception.notFound.ProductNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartItemService {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<CartItemDto> findByMember(Member member) {
        List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());
        return cartItems.stream().map(CartItemDto::from).collect(Collectors.toList());
    }

    public Long add(Member member, CartItemRequest cartItemRequest) {
        Product product = productDao.findById(cartItemRequest.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return cartItemDao.save(new CartItem(member, product));
    }

    public void updateQuantity(Member member, Long id, CartItemQuantityUpdateRequest request) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(CartItemNotFountException::new);
        cartItem.checkOwner(member);

        if (request.getQuantity() == 0) {
            cartItemDao.deleteById(id);
            return;
        }

        cartItem.changeQuantity(request.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(CartItemNotFountException::new);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
