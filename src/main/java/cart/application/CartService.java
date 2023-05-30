package cart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.Cart;
import cart.domain.Member;

@Service
public class CartService {

    private final CartItemService cartItemService;

    public CartService(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Transactional(readOnly = true)
    public Cart getCartOf(Member member) {
        return new Cart(member, cartItemService.findByMember(member));
    }

    @Transactional
    public void save(Cart cart) {
        cartItemService.removeAllOf(cart.getOwner());
        cartItemService.add(cart.getItems());
    }
}
