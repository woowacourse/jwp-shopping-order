package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public class Fixture {
    public static final Member GOLD_MEMBER = new Member(1L, "a@a.com", "1234", 1);
    public static final Member SILVER_MEMBER = new Member(2L, "b@b.com", "1234", 2);
    public static final Member BRONZE_MEMBER = new Member(3L, "c@c.com", "1234", 3);

    public static final Product CHICKEN_PRODUCT = new Product(1L, "치킨", 10000, "사진");
    public static final Product PIZZA_PRODUCT = new Product(2L, "피자", 20000, "사진");

    public static final CartItem CART_ITEM1 = new CartItem(1L, 2, CHICKEN_PRODUCT, GOLD_MEMBER);
    public static final CartItem CART_ITEM2 = new CartItem(2L, 4, PIZZA_PRODUCT, GOLD_MEMBER);
}
