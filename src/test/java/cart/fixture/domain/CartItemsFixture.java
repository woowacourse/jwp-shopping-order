package cart.fixture.domain;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;

import java.util.List;

public abstract class CartItemsFixture {

    public static CartItems 장바구니_상품_목록(List<CartItem> 장바구니_상품_목록) {
        return CartItems.from(장바구니_상품_목록);
    }
}
