package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.ProductDao;
import cart.domain.cart.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        return productDao.findAll().stream()
                .map(this::toProduct)
                .collect(toList());
    }

    private Product toProduct(final ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getImageUrl(), entity.getPrice());
    }

    public Optional<Product> findById(final Long productId) {
        return productDao.findById(productId).map(this::toProduct);
    }

    public Product save(final Product product) {
        if (Objects.isNull(product.getId())) {
            final ProductEntity productEntity = productDao.insert(ProductEntity.from(product));
            return toProduct(productEntity);
        }
        productDao.update(ProductEntity.from(product));
        return product;
    }

    public void deleteById(final Long productId) {
        productDao.deleteById(productId);
    }
}
