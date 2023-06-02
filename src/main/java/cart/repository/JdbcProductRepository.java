package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public JdbcProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Long create(final Product product) {
        return productDao.create(ProductEntity.from(product));
    }

    @Override
    public Product findById(final Long id) {
        return productDao.findById(id)
                .orElseThrow(NoSuchElementException::new)
                .toDomain();
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll().stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(final Long id, final Product product) {
        productDao.update(id, product);
    }

    @Override
    public void deleteById(final Long id) {
        productDao.deleteById(id);
    }
}
