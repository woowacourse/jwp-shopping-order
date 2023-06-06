package cart.repository;

import cart.domain.product.Product;
import cart.exception.CartItemException;
import cart.repository.dao.ProductDao;
import cart.repository.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

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

    public long createProduct(Product product) {
        ProductEntity productEntity = new ProductEntity.Builder()
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
        return productDao.createProduct(productEntity);
    }

    public void updateProduct(long productId, Product product) {
        ProductEntity productEntity = new ProductEntity.Builder()
                .id(productId)
                .name(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
        productDao.updateProduct(productEntity);
    }

    public void deleteProduct(long productId) {
        productDao.deleteProduct(productId);
    }
}
