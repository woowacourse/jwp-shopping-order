package cart.support;

import cart.dao.ProductDao;
import cart.domain.Product;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ProductTestSupport {

    private static String defaultName = "product";
    private static int defaultPrice = 5_000;
    private static String defaultImageUrl = "imageUrl";

    private final ProductDao productDao;

    public ProductTestSupport(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductBuilder builder() {
        return new ProductBuilder();
    }

    public final class ProductBuilder {

        private Long id;
        private String name;
        private Integer price;
        private String imageUrl;

        public ProductBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(final Integer price) {
            this.price = price;
            return this;
        }

        public ProductBuilder imageUrl(final String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Product build() {
            Product product = make();
            Long productId = productDao.add(product);
            return new Product(productId, product.getName(), product.getPrice(), product.getImageUrl());
        }

        public Product make() {
            return new Product(
                    id == null ? null : id,
                    name == null ? defaultName + UUID.randomUUID() : name,
                    price == null ? defaultPrice : price,
                    imageUrl == null ? defaultImageUrl : imageUrl);
        }
    }
}
