package cart.fixture.entity;

import cart.dao.entity.ProductEntity;

import java.math.BigDecimal;

public abstract class ProductEntityFixture {

    public static ProductEntity 상품_엔티티(String 상품명, int 가격) {
        return new ProductEntity(상품명, BigDecimal.valueOf(가격), "https://image.png");
    }
}
