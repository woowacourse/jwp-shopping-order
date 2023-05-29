package cart.fixture;

import cart.domain.product.Product;

public class ProductFixture {

    public static Product createProduct() {
        return new Product(1L, "치킨", 20000, "img.img", false, 0);
    }

    public static Product createProduct2() {
        return new Product(2L, "피자", 10000, "img2.img", true, 10);
    }

    public static Product createProduct3() {
        return new Product("샐러드", 5000, "img3.img");
    }

    public static Product createProduct(final Long id, final String name) {
        return new Product(id, name, 100, "imgCustom.img", false, 0);
    }
}
