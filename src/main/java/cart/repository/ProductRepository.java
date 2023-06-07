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
        return productDao.findById(id);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Long add(Product product) {
        return productDao.save(product);
    }

    public void update(Product product) {
        productDao.update(product.getId(), product);
    }

    public void remove(Long id) {
        productDao.deleteById(id);
    }
}
