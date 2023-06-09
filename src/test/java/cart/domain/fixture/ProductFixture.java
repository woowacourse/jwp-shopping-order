package cart.domain.fixture;

import cart.domain.Product;

public class ProductFixture {

    public static Product PRODUCT = Product.of("치킨", 10_000, "http://example.com/chicken.jpg");
    public static Product OTHER_PRODUCT = Product.of("피자", 15_000, "http://example.com/pizza.jpg");
    public static Product ANOTHER_PRODUCT = Product.of("보쌈", 15_000,
        "http://example.com/pizza.jpg");
}
