package cart.fixture;

import cart.entity.ProductEntity;

public class ProductEntityFixture {

    public static final ProductEntity CHICKEN = new ProductEntity("치킨", 10_000, "http://example.com/chicken.jpg", 10);
    public static final ProductEntity PIZZA = new ProductEntity("피자", 15_000, "http://example.com/pizza.jpg", 10);
}
