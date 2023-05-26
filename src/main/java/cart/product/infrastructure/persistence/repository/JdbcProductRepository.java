package cart.product.infrastructure.persistence.repository;

import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import cart.product.infrastructure.persistence.dao.ProductDao;
import cart.product.infrastructure.persistence.entity.ProductEntity;
import cart.product.infrastructure.persistence.mapper.ProductEntityMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public JdbcProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Long save(Product product) {
        ProductEntity entity = ProductEntityMapper.toEntity(product);
        return productDao.save(entity);
    }

    @Override
    public void update(Product product) {
        ProductEntity entity = ProductEntityMapper.toEntity(product);
        productDao.update(entity.getId(), entity);
    }

    @Override
    public void delete(Long productId) {
        productDao.delete(productId);
    }

    @Override
    public Product findById(Long id) {
        return ProductEntityMapper.toDomain(productDao.findById(id)
                .orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll().stream()
                .map(ProductEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
