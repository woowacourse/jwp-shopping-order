package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import java.util.List;
import org.mockito.internal.matchers.Or;

public class Fixture {

    public static final Member MEMBER_A = new Member(1L,"gold", "a@a.com", "1234");
    public static final Member MEMBER_B = new Member(2L, "silver", "b@b.com", "1234");
    public static final Member MEMBER_C = new Member(3L, "bronze", "c@c.com", "1234");

    public static final Product PIZZA = new Product(1L, "피자", 13_000, "피자사진");
    public static final Product SALAD = new Product(2L, "샐러드", 20_000, "샐러드사진");
    public static final Product CHICKEN = new Product(3L, "치킨", 10_000, "치킨사진");

    public static final CartItem A_CART_ITEM_CHICKEN = new CartItem(1L, 2, CHICKEN, MEMBER_A);
    public static final CartItem A_CART_ITEM_SALAD = new CartItem(2L, 4, SALAD, MEMBER_A);
    public static final CartItem B_CART_ITEM_PIZZA = new CartItem(3L, 5, PIZZA, MEMBER_B);

    public static final Order A_ORDER = new Order(10_6000, MEMBER_A, List.of(A_CART_ITEM_CHICKEN, A_CART_ITEM_SALAD));
    public static final Order B_ORDER = new Order(85_000, MEMBER_B, List.of(B_CART_ITEM_PIZZA));
}
