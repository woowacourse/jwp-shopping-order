package cart.fixture;

import cart.domain.cart.Product;
import cart.entity.ProductEntity;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static final Product 상품_8900원 = new Product("pizza1", "pizza1.png", 8900L);
    public static final Product 상품_18900원 = new Product("pizza2", "pizza2.png", 18900L);
    public static final Product 상품_28900원 = new Product("pizza3", "pizza3.png", 28900L);

    public static final ProductEntity 상품_8900원_엔티티 = new ProductEntity(
            "pizza1", "pizza1.png", 8900L
    );
    public static final ProductEntity 상품_18900원_엔티티 = new ProductEntity(
            "pizza2", "pizza2.png", 18900L
    );
    public static final ProductEntity 상품_28900원_엔티티 = new ProductEntity(
            "pizza3", "pizza3.png", 28900L
    );
}
