package cart.fixture;

import cart.entity.ProductEntity;

public class ProductFixture {

    public static ProductEntity chicken = generateChicken(null);

    public static ProductEntity generateChicken(Long id) {
        return new ProductEntity(id, "치킨", 10_000, "이미지", true, 5);
    }

    public static ProductEntity fork = generateFork(null);

    public static ProductEntity generateFork(Long id) {
        return new ProductEntity(id, "삼겹살", 15_000, "이미지", false, 0);
    }
}
