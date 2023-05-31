package cart.fixture;

import static cart.fixture.MemberFixture.MEMBER;

import cart.domain.CartItem;
import cart.domain.Product;

public class CartItemFixture {


    public static final CartItem CHICKEN = new CartItem(MEMBER,
            new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg")
    );

    public static final CartItem PIZZA = new CartItem(MEMBER,
            new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg")
    );
}
