package cart.fixtures;

import static cart.fixtures.MemberFixtures.*;
import static cart.fixtures.ProductFixtures.*;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.SALAD;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.CartItemResponse;
import cart.dto.ProductCartItemResponse;

public class CartItemFixtures {

    public static class Dooly_CartItem1 {
        public static final Long ID = 1L;
        public static final int QUANTITY = 2;
        public static final Product PRODUCT = CHICKEN.ENTITY();
        public static final Member MEMBER = Dooly.ENTITY();
        public static final int PRICE = PRODUCT.getPrice() * QUANTITY;

        public static CartItem ENTITY() {
            return new CartItem(ID, QUANTITY, PRODUCT, MEMBER);
        }
        public static final CartItemResponse RESPONSE = CartItemResponse.from(ENTITY());
        public static final ProductCartItemResponse PRODUCT_CART_ITEM_RESPONSE = ProductCartItemResponse.createContainsCartItem(PRODUCT, ENTITY());
    }

    public static class Dooly_CartItem2 {
        public static final Long ID = 2L;
        public static final int QUANTITY = 4;
        public static final Product PRODUCT = SALAD.ENTITY();
        public static final Member MEMBER = Dooly.ENTITY();
        public static final int PRICE = PRODUCT.getPrice() * QUANTITY;

        public static CartItem ENTITY() {
            return new CartItem(ID, QUANTITY, PRODUCT, MEMBER);
        }
        public static final CartItemResponse RESPONSE = CartItemResponse.from(ENTITY());
    }

    public static class Ber_CartItem1 {
        public static final Long ID = 3L;
        public static final int QUANTITY = 5;
        public static final Product PRODUCT = CHICKEN.ENTITY();
        public static final Member MEMBER = Ber.ENTITY();

        public static CartItem ENTITY() {
            return new CartItem(ID, QUANTITY, PRODUCT, MEMBER);
        }
        public static final CartItemResponse RESPONSE = CartItemResponse.from(ENTITY());
    }

    public static class Ber_CartItem2 {
        public static final Long ID = 4L;
        public static final int QUANTITY = 5;
        public static final Product PRODUCT = PIZZA.ENTITY();
        public static final Member MEMBER = Ber.ENTITY();

        public static CartItem ENTITY() {
            return new CartItem(ID, QUANTITY, PRODUCT, MEMBER);
        }
        public static final CartItemResponse RESPONSE = CartItemResponse.from(ENTITY());
    }

    public static class Bixx_CartItem1 {
        public static final Long ID = 5L;
        public static final int QUANTITY = 5;
        public static final Product PRODUCT = SALAD.ENTITY();
        public static final Member MEMBER = Bixx.ENTITY();

        public static CartItem ENTITY() {
            return new CartItem(ID, QUANTITY, PRODUCT, MEMBER);
        }
        public static final CartItemResponse RESPONSE = CartItemResponse.from(ENTITY());
    }
}
