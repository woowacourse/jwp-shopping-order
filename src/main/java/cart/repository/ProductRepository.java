package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Optional<Product> findById(Long id) {
        return productDao.findById(id)
                .map(ProductEntity::toDomain);
    }

    public List<Product> findAll() {
        return productDao.findAll().stream()

                .map(ProductEntity::toDomain)
                .collect(toList());
    }

    public Long save(Product product) {
        return productDao.save(ProductEntity.from(product));
    }

    public void update(Product product) {
        productDao.update(ProductEntity.from(product));
    }

    public void deleteById(Long id) {
        productDao.deleteById(id);
    }
}
