package cart.product.application;

import cart.product.Product;
import cart.product.infrastructure.ProductDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryDaoImpl implements ProductRepository {
    private final ProductDao productDao;

    public ProductRepositoryDaoImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public Product getProductById(Long productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Long createProduct(Product product) {
        return productDao.createProduct(product);
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        productDao.updateProduct(productId, product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
