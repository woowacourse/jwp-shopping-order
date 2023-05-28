package cart.fixtures;

import cart.domain.Product;
import cart.dto.ProductCartItemResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;

public class ProductFixtures {

    public static class CHICKEN {
        public static final Long ID = 1L;
        public static final String NAME = "치킨";
        public static final int PRICE = 10000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80";

        public static final Product DOMAIN = new Product(NAME, PRICE, IMAGE_URL);
        public static final Product ENTITY = new Product(ID, NAME, PRICE, IMAGE_URL);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE_URL);
        public static final ProductResponse RESPONSE = ProductResponse.from(ENTITY);
    }

    public static class SALAD {
        public static final Long ID = 2L;
        public static final String NAME = "샐러드";
        public static final int PRICE = 2000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80";

        public static final Product DOMAIN = new Product(NAME, PRICE, IMAGE_URL);
        public static final Product ENTITY = new Product(ID, NAME, PRICE, IMAGE_URL);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE_URL);
        public static final ProductResponse RESPONSE = ProductResponse.from(ENTITY);
    }

    public static class PIZZA {
        public static final Long ID = 3L;
        public static final String NAME = "피자";
        public static final int PRICE = 13000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80";

        public static final Product DOMAIN = new Product(NAME, PRICE, IMAGE_URL);
        public static final Product ENTITY = new Product(ID, NAME, PRICE, IMAGE_URL);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE_URL);
        public static final ProductResponse RESPONSE = ProductResponse.from(ENTITY);
    }

    public static class PANCAKE {
        public static final Long ID = 4L;
        public static final String NAME = "팬케이크";
        public static final int PRICE = 8000;
        public static final String IMAGE_URL = "https://images.unsplash.com/photo-1544726982-b414d58fabaa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVEJThDJUFDJUVDJUJDJTgwJUVDJTlEJUI0JUVEJTgxJUFDfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60";

        public static final Product DOMAIN = new Product(NAME, PRICE, IMAGE_URL);
        public static final Product ENTITY = new Product(ID, NAME, PRICE, IMAGE_URL);
        public static final ProductRequest REQUEST = new ProductRequest(NAME, PRICE, IMAGE_URL);
        public static final ProductResponse RESPONSE = ProductResponse.from(ENTITY);
        public static final ProductCartItemResponse PRODUCT_CART_ITEM_RESPONSE = ProductCartItemResponse.createOnlyProduct(ENTITY);
    }
}
