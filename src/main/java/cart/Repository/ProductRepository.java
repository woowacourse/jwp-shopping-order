package cart.Repository;

import cart.dao.ProductDao;
import cart.domain.Product.Product;
import cart.entity.ProductEntity;
import cart.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static cart.Repository.mapper.ProductMapper.*;

@Repository
public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }


    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        if(productEntities.isEmpty()){
            return Collections.emptyList();
        }
        return toProducts(productEntities);
    }

    public Product getProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId)
                .orElseThrow(() -> new NotFoundException.Product(productId));

        return toProduct(productEntity);
    }

    public Long createProduct(Product product) {
        ProductEntity productEntity = toProductEntity(product);
        return productDao.createProduct(productEntity);
    }


    public void updateProduct(Long productId, Product product) {
        ProductEntity productEntity = toProductEntity(product);

        try {
            productDao.updateProduct(productId, productEntity);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException.Product(productId);
        }
    }

    public void deleteProduct(Long productId) {
        try {
            productDao.deleteProduct(productId);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException.Product(productId);
        }
    }
}
