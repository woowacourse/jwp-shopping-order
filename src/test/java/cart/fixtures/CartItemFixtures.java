package cart.fixtures;

import cart.domain.cartitem.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemResponse;

import static cart.fixtures.MemberFixtures.MemberA;
import static cart.fixtures.MemberFixtures.MemberB;
import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PIZZA;
import static cart.fixtures.ProductFixtures.SALAD;

public class CartItemFixtures {

    public static class MemberA_CartItem1 {
        public static final Long ID = 1L;
        public static final int QUANTITY = 2;
        public static final Product PRODUCT = CHICKEN.ENTITY;
        public static final Member MEMBER = MemberA.ENTITY;

        public static final CartItem ENTITY = CartItem.of(ID, QUANTITY, PRODUCT, MEMBER);
        public static final CartItemResponse RESPONSE = CartItemResponse.of(ENTITY);
    }

    public static class MemberA_CartItem2 {
        public static final Long ID = 2L;
        public static final int QUANTITY = 4;
        public static final Product PRODUCT = SALAD.ENTITY;
        public static final Member MEMBER = MemberA.ENTITY;

        public static final CartItem ENTITY = CartItem.of(ID, QUANTITY, PRODUCT, MEMBER);
        public static final CartItemResponse RESPONSE = CartItemResponse.of(ENTITY);
    }

    public static class MemberB_CartItem1 {
        public static final Long ID = 3L;
        public static final int QUANTITY = 5;
        public static final Product PRODUCT = PIZZA.ENTITY;
        public static final Member MEMBER = MemberB.ENTITY;

        public static final CartItem ENTITY = CartItem.of(ID, QUANTITY, PRODUCT, MEMBER);
        public static final CartItemResponse RESPONSE = CartItemResponse.of(ENTITY);
    }
}
