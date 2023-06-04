package cart.fixture;

import static cart.fixture.MemberFixture.MEMBER;

import cart.domain.CartItem;
import cart.domain.Product;

public class CartItemFixture {


    public static final CartItem CHICKEN = CartItem.of(MEMBER,
            new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg")
    );

    public static final CartItem PIZZA = CartItem.of(MEMBER,
            new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg")
    );

    public static final CartItem CHICKEN_QUANTITY = CartItem.of(
            new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg"), 5
    );

    public static final CartItem PIZZA_QUANTITY = CartItem.of(
            new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg"), 7
    );

    public static final CartItem SALAD_QUANTITY = CartItem.of(
            new Product(3L, "샐러드", 20_000, "http://example.com/salad.jpg"), 10
    );
}
