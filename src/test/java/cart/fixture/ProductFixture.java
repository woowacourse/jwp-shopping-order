package cart.fixture;

import cart.domain.Product;

public class ProductFixture {

    public static final Product CHICKEN = new Product("치킨", 10_000, "http://example.com/chicken.jpg");

    public static final Product CHICKEN_WITH_ID = new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg");
    public static final Product PIZZA_WITH_ID = new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg");
    public static final Product SALAD_WITH_ID = new Product(3L, "샐러드", 20_000, "http://example.com/salad.jpg");
}
