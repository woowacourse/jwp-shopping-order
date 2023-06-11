package cart.domain.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public JdbcProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Optional<Product> getProductById(long productId) {
        try {
            ProductEntity productEntity = productDao.getProductById(productId)
                    .orElseThrow(CartItemException.NotFound::new);
            Product product = new Product(
                    productEntity.getId(),
                    productEntity.getName(),
                    productEntity.getPrice(),
                    productEntity.getImageUrl()
            );
            return Optional.of(product);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Product>> getAllProducts() {
        try {
            List<ProductEntity> productEntities = productDao.getAllProducts();
            List<Product> products = productEntities.stream()
                    .map(productEntity -> new Product(
                            productEntity.getId(),
                            productEntity.getName(),
                            productEntity.getPrice(),
                            productEntity.getImageUrl())
                    ).collect(Collectors.toList());
            return Optional.of(products);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public long createProduct(Product product) {
        ProductEntity productEntity = new ProductEntity.Builder()
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
        return productDao.createProduct(productEntity);
    }

    @Override
    public void updateProduct(long productId, Product product) {
        ProductEntity productEntity = new ProductEntity.Builder()
                .id(productId)
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
        productDao.updateProduct(productEntity);
    }

    @Override
    public void deleteProduct(long productId) {
        productDao.deleteProduct(productId);
    }
}
