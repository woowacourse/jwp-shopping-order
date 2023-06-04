package cart.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long create(Product product) {
        ProductEntity productEntity = ProductEntity.from(product);
        return productDao.save(productEntity);
    }

    public List<Product> findAll() {
        List<ProductEntity> all = productDao.findAll();
        return all.stream()
                .map(ProductEntity::toProduct)
                .collect(Collectors.toList());
    }

    public Product findById(Long id) {
        ProductEntity productEntity = productDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품은 존재하지 않습니다."));
        return productEntity.toProduct();
    }

    public void update(Product product) {
        ProductEntity productEntity = ProductEntity.from(product);
        productDao.update(productEntity);
    }

    public void delete(Product product) {
        productDao.deleteById(product.getId());
    }
}
