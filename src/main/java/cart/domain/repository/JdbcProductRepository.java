package cart.domain.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public JdbcProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product getProductById(long productId) {
        ProductEntity productEntity = productDao.getProductById(productId)
                .orElseThrow(CartItemException.NotFound::new);
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl()
        );
    }

    @Override
    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream()
                .map(productEntity -> new Product(
                        productEntity.getId(),
                        productEntity.getName(),
                        productEntity.getPrice(),
                        productEntity.getImageUrl())
                ).collect(Collectors.toList());
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
