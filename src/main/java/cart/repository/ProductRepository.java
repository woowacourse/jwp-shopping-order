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

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public long createProduct(final Product product) {
        return productDao.createProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts()
                .stream()
                .map(ProductEntity::toProduct)
                .collect(Collectors.toList());
    }

    public Product getProductById(final Long id) {
        return productDao.getProductById(id).toProduct();
    }

    public void updateProduct(final Long id, final Product product) {
        productDao.updateProduct(id, product);
    }

    public void deleteProduct(final Long id) {
        productDao.deleteProduct(id);
    }
}
