package cart.fixtures;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static cart.fixtures.OrderFixtures.Dooly_Order2;

import cart.domain.order.domain.OrderItem;

public class OrderItemFixtures {

    public static class Dooly_Order_Item1 {
        public static final Long ID = 1L;
        public static final String NAME = Dooly_CartItem1.PRODUCT.getName();
        public static final int PRICE = Dooly_CartItem1.PRODUCT.getPrice();
        public static final String IMAGE_URL = Dooly_CartItem1.PRODUCT.getImageUrl();
        public static final int QUANTITY = Dooly_CartItem1.QUANTITY;
        public static final Long PRODUCT_ID = Dooly_CartItem1.PRODUCT.getId();
        public static OrderItem ENTITY() {
            return new OrderItem(ID, Dooly_Order1.ENTITY(), PRODUCT_ID, NAME, PRICE, IMAGE_URL, QUANTITY);
        }
    }

    public static class Dooly_Order_Item2 {
        public static final Long ID = 2L;
        public static final String NAME = Dooly_CartItem2.PRODUCT.getName();
        public static final int PRICE = Dooly_CartItem2.PRODUCT.getPrice();
        public static final String IMAGE_URL = Dooly_CartItem2.PRODUCT.getImageUrl();
        public static final int QUANTITY = Dooly_CartItem2.QUANTITY;
        public static final Long PRODUCT_ID = Dooly_CartItem2.PRODUCT.getId();
        public static OrderItem ENTITY() {
            return new OrderItem(ID, Dooly_Order1.ENTITY(), PRODUCT_ID, NAME, PRICE, IMAGE_URL, QUANTITY);
        }
    }

    public static class Dooly_Order_Item3 {
        public static final Long ID = 3L;
        public static final String NAME = Dooly_CartItem3.PRODUCT.getName();
        public static final int PRICE = Dooly_CartItem3.PRODUCT.getPrice();
        public static final String IMAGE_URL = Dooly_CartItem3.PRODUCT.getImageUrl();
        public static final int QUANTITY = Dooly_CartItem3.QUANTITY;
        public static final Long PRODUCT_ID = Dooly_CartItem3.PRODUCT.getId();
        public static OrderItem ENTITY() {
            return new OrderItem(ID, Dooly_Order2.ENTITY(), PRODUCT_ID, NAME, PRICE, IMAGE_URL, QUANTITY);
        }
    }
}
