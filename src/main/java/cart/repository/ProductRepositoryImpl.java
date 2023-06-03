package cart.repository;

import cart.dao.ProductDao;
import cart.dao.dto.PageInfo;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDao productDao;

    public ProductRepositoryImpl(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product findProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        return convertEntityToProduct(productEntity);
    }

    public Long createProduct(Product product) {
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, Product product) {
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }

    public PageInfo findPageInfo(int size, int page) {
        return productDao.findPageInfo(size, page);
    }

    public List<Product> findProductsByPage(int size, int page) {
        List<ProductEntity> productEntities = productDao.findProductsInCurrentPage(size, page);
        return productEntities.stream()
                .map(this::convertEntityToProduct)
                .collect(Collectors.toList());
    }

    private Product convertEntityToProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    public List<Product> getAllProducts() {
        List<ProductEntity> allProducts = productDao.getAllProducts();
        return allProducts.stream()
                .map(this::convertEntityToProduct)
                .collect(Collectors.toList());
    }
}
