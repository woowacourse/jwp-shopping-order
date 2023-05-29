package cart.fixture;

import cart.domain.Product;
import cart.domain.ProductStock;
import cart.domain.Stock;

public class ProductStockFixture {

    public static final ProductStock CHICKEN = new ProductStock(
            new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg"),
            new Stock(10)
    );
    public static final ProductStock PIZZA = new ProductStock(
            new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg"),
            new Stock(10)
    );
}
