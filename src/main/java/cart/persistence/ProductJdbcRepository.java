package cart.persistence;

import cart.application.mapper.ProductMapper;
import cart.application.repository.ProductRepository;
import cart.domain.Product;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    @Autowired
    private final ProductDao productDao;

    public ProductJdbcRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public long create(final Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        return productDao.create(entity);
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> entities = productDao.findAll();
        return entities.stream()
                .map(ProductMapper::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(final long id) {
        Optional<ProductEntity> entity = productDao.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ProductMapper.toProduct(entity.get()));
    }

    @Override
    public void update(final Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        productDao.update(entity);
    }

    @Override
    public void deleteById(final long id) {
        productDao.deleteById(id);
    }
}
