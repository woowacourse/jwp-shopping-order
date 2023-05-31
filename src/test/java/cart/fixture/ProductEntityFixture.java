package cart.fixture;

import cart.entity.ProductEntity;

public abstract class ProductEntityFixture {

    public static ProductEntity 상품_엔티티(String 상품명, int 가격) {
        return new ProductEntity(상품명, 가격, "https://image.png");
    }
}
