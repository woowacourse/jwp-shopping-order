package cart.fixture;

import cart.domain.CartItem;
import cart.domain.Product;
import cart.dto.CartItemResponse;

public class CartItemResponseFixture {

    public static final CartItemResponse CHICKEN = CartItemResponse.of(new CartItem(
            1L,
            MemberFixture.MEMBER,
            new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg"),
            10
    ));

    public static final CartItemResponse PIZZA = CartItemResponse.of(new CartItem(
            2L,
            MemberFixture.MEMBER,
            new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg"),
            10
    ));
}
