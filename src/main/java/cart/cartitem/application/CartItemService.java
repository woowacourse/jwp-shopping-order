package cart.cartitem.application;

import cart.cartitem.application.dto.AddCartItemCommand;
import cart.cartitem.application.dto.UpdateCartItemQuantityCommand;
import cart.cartitem.domain.CartItem;
import cart.cartitem.infrastructure.persistence.dao.CartItemDao;
import cart.cartitem.presentation.dto.CartItemResponse;
import cart.member.domain.Member;
import cart.product.infrastructure.persistence.dao.ProductDao;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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
        return cartItems.stream().map(CartItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long add(AddCartItemCommand command) {
        return cartItemDao.save(new CartItem(
                command.getMember(),
                productDao.getProductById(command.getProductId()))
        );
    }

    public void updateQuantity(UpdateCartItemQuantityCommand command) {
        CartItem cartItem = cartItemDao.findById(command.getId());
        cartItem.checkOwner(command.getMember());

        if (command.getQuantity() == 0) {
            cartItemDao.deleteById(command.getId());
            return;
        }

        cartItem.changeQuantity(command.getQuantity());
        cartItemDao.updateQuantity(cartItem);
    }

    public void remove(Member member, Long id) {
        CartItem cartItem = cartItemDao.findById(id);
        cartItem.checkOwner(member);

        cartItemDao.deleteById(id);
    }
}
