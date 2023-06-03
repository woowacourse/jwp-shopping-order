package cart.fixture;

import cart.domain.Product;
import cart.persistence.entity.ProductEntity;

public class ProductFixture {

    public static class 피자_20000원 {

        private static final String NAME = "피자";
        private static final int PRICE = 20_000;
        private static final String IMAGE_URL = "https://pizza";

        public static final ProductEntity ENTITY = new ProductEntity(NAME, PRICE, IMAGE_URL);
        public static final Product PRODUCT = new Product(NAME, PRICE, IMAGE_URL);
    }

    public static class 치킨_15000원 {

        private static final String NAME = "치킨";
        private static final int PRICE = 15_000;
        private static final String IMAGE_URL = "https://pizza";

        public static final ProductEntity ENTITY = new ProductEntity(NAME, PRICE, IMAGE_URL);
        public static final Product PRODUCT = new Product(NAME, PRICE, IMAGE_URL);
    }
}
