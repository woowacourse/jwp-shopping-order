package cart.db.repository;

import cart.db.dao.ProductDao;
import cart.db.entity.ProductEntity;
import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cart.db.mapper.ProductMapper.toDomain;
import static cart.db.mapper.ProductMapper.toEntity;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long save(final Product product) {
        ProductEntity productEntity = toEntity(product);
        return productDao.create(productEntity);
    }

    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.findAll();
        return toDomain(productEntities);
    }

    public List<Product> findByIds(final List<Long> ids) {
        List<ProductEntity> productEntities = productDao.findByIds(ids);
        return toDomain(productEntities);
    }

    public Product findById(final Long id) {
        ProductEntity productEntity = productDao.findById(id);
        return toDomain(productEntity);
    }

    public void updateById(final Long id, final Product product) {
        productDao.update(id, toEntity(product));
    }

    public void deleteById(final Long id) {
        productDao.updateToDelete(id);
    }
}
