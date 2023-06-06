package cart.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;
import cart.exception.notfound.ProductNotFoundException;
import cart.repository.mapper.ProductMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
                .map(ProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        ProductEntity productEntity = productDao.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductMapper.toDomain(productEntity);
    }

    public Long save(Product product) {
        ProductEntity productEntity = ProductMapper.toEntity(product);
        return productDao.save(productEntity);
    }

    public void update(Product product) {
        ProductEntity productEntity = ProductMapper.toEntity(product);
        productDao.update(productEntity);
    }

    public void delete(Long productId) {
        productDao.delete(productId);
    }
}
