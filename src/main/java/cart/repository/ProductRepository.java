package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream()
                .map(productEntity -> new Product(productEntity.getId(), productEntity.getName(),
                        productEntity.getPrice(), productEntity.getImageUrl()))
                .collect(Collectors.toList());
    }

    public Product getProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        return new Product(productEntity.getId(), productEntity.getName(),
                productEntity.getPrice(), productEntity.getImageUrl());
    }

    public List<Product> findAllByIds(List<Long> productIds) {
        List<ProductEntity> productEntities = productDao.findAllByIds(productIds);
        return productEntities.stream()
                .map(productEntity -> new Product(productEntity.getId(), productEntity.getName(),
                        productEntity.getPrice(), productEntity.getImageUrl()))
                .collect(Collectors.toList());
    }

    public Long createProduct(Product product) {
        ProductEntity productEntity = new ProductEntity(product.getName(), product.getPrice(), product.getImageUrl());
        return productDao.createProduct(productEntity);
    }

    public void updateProduct(Long productId, Product product) {
        ProductEntity productEntity = new ProductEntity(product.getName(), product.getPrice(), product.getImageUrl());
        productDao.updateProduct(productId, productEntity);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
