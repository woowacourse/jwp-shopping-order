package cart.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import cart.dao.ProductDao;
import cart.domain.Product;

@Repository
public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product findById(Long id) {
        return productDao.getProductById(id);
    }

    public List<Product> findAll() {
        return productDao.getAllProducts();
    }

    public Long add(Product product) {
        return productDao.createProduct(product);
    }

    public void update(Product product) {
        productDao.updateProduct(product.getId(), product);
    }

    public void remove(Long id) {
        productDao.deleteProduct(id);
    }
}
