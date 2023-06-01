package cart.fixture;

import cart.domain.product.Product;

public class ProductFixture {

    public static Product chicken = generateChicken(null);

    public static Product generateChicken(Long id) {
        return new Product(id, "치킨", 10_000, "이미지", true, 5);
    }

    public static Product fork = generateFork(null);

    public static Product generateFork(Long id) {
        return new Product(id, "삼겹살", 15_000, "이미지", false, 0);
    }
}
