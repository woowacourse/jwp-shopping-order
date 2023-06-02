package cart.fixture;

import java.util.List;

import cart.domain.CartItem;
import cart.domain.CartItems;

public class CartItemsFixture {

    public static final CartItems ORDER1_CARTITEMS = CartItems.of(
            List.of(
                    new CartItem(ProductFixture.CHICKEN_WITH_ID, 2),
                    new CartItem(ProductFixture.PIZZA_WITH_ID, 3)
            )
    );

    public static final CartItems ORDER2_CARTITEMS = CartItems.of(
            List.of(
                    new CartItem(ProductFixture.CHICKEN_WITH_ID, 4),
                    new CartItem(ProductFixture.PIZZA_WITH_ID, 5),
                    new CartItem(ProductFixture.SALAD_WITH_ID, 6)
            )
    );
}
