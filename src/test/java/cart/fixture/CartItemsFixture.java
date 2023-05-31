package cart.fixture;

import cart.domain.CartItem;
import cart.domain.CartItems;

import java.util.List;

public abstract class CartItemsFixture {

    public static CartItems 장바구니_상품_목록(List<CartItem> 장바구니_상품_목록) {
        return CartItems.from(장바구니_상품_목록);
    }
}
