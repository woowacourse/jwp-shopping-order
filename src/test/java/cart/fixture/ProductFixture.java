package cart.fixture;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.entity.ProductEntity;

public class ProductFixture {
    public static final class 치킨 {
        private static final long ID = 1L;
        private static final String NAME = "치킨";
        private static final Integer PRICE = 18000;
        private static final String IMAGE = "https://i.namu.wiki/i/YVm0x8WHfLBtSyejD01_GTV1ITfWOJ-XODZzVTQPr386JsiBaz6Ucl1tKKxZmHiYStf_sXZBmK7AEXkEA18Tsg.webp";

        public static final Product PRODUCT = new Product(ID, NAME, PRICE, IMAGE);
        public static final ProductEntity ENTITY = new ProductEntity(ID, NAME, PRICE, IMAGE);
        public static final ProductResponse RESPONSE = new ProductResponse(ID, NAME, PRICE, IMAGE);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE);
    }

    public static final class 피자 {
        private static final long ID = 2L;
        private static final String NAME = "피자";
        private static final Integer PRICE = 22000;
        private static final String IMAGE = "https://cdn.dominos.co.kr/admin/upload/goods/20200311_x8StB1t3.jpg";

        public static final Product PRODUCT = new Product(ID, NAME, PRICE, IMAGE);
        public static final ProductEntity ENTITY = new ProductEntity(ID, NAME, PRICE, IMAGE);
        public static final ProductResponse RESPONSE = new ProductResponse(ID, NAME, PRICE, IMAGE);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE);
    }

    public static final class 핫도그 {
        private static final long ID = 3L;
        private static final String NAME = "핫도그";
        private static final Integer PRICE = 2500;
        private static final String IMAGE = "https://recipe1.ezmember.co.kr/cache/recipe/2021/12/24/62c543074ff14293b98273a8c6cd55461.jpg";

        public static final Product PRODUCT = new Product(ID, NAME, PRICE, IMAGE);
        public static final ProductEntity ENTITY = new ProductEntity(ID, NAME, PRICE, IMAGE);
        public static final ProductResponse RESPONSE = new ProductResponse(ID, NAME, PRICE, IMAGE);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE);
    }
}
