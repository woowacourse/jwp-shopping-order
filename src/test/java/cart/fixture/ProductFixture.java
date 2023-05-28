package cart.fixture;

import cart.domain.product.Product;

public class ProductFixture {

    public static Product createProduct() {
        return new Product("치킨", 20000, "img.img");
    }

    public static Product createProduct2() {
        return new Product("피자", 10000, "img2.img");
    }

    public static Product createProduct3() {
        return new Product("샐러드", 5000, "img3.img");
    }

    public static Product createProduct(final Long id, final String name) {
        return new Product(id, name, 100, "imgCustom.img", false, 0);
    }
}
