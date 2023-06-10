package cart.fixture;

import cart.domain.Product;

public class ProductFixture {
    public static final Product PRODUCT_1 = new Product(1L, "치킨", 10_000, "url1");
    public static final Product PRODUCT_2 = new Product(2L, "피자", 20_000, "url2");
    public static final Product PRODUCT_3 = new Product(3L, "햄버거", 5_000, "url3");
    public static final Product PRODUCT_1_2 = new Product(1L, "햄버거", 5_000, "url3");
}
