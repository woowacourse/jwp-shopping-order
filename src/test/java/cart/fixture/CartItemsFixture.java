package cart.fixture;

import java.util.List;

import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Product;

public class CartItemsFixture {

    public static final CartItems ORDER1_CARTITEMS = CartItems.of(
            List.of(
                    CartItem.of(ProductFixture.CHICKEN_WITH_ID, 2),
                    CartItem.of(ProductFixture.PIZZA_WITH_ID, 3)
            )
    );

    public static final CartItems ORDER2_CARTITEMS = CartItems.of(
            List.of(
                    CartItem.of(ProductFixture.CHICKEN_WITH_ID, 4),
                    CartItem.of(ProductFixture.PIZZA_WITH_ID, 5),
                    CartItem.of(ProductFixture.SALAD_WITH_ID, 6)
            )
    );

    public static final CartItems ORDER3_CARTITEMS = CartItems.of(
            List.of(
                    CartItem.of(new Product(4L, null, 0, null), 3)
            )
    );

    public static final CartItems ORDER4_CARTITEMS = CartItems.of(
            List.of(
                    CartItem.of(new Product(1L, null, 0, null), 11)
            )
    );

    public static final CartItems ORDER5_CARTITEMS = CartItems.of(
            List.of(
                    CartItem.of(new Product(1L, null, 0, null), 10),
                    CartItem.of(new Product(2L, null, 0, null), 11)
            )
    );
}
