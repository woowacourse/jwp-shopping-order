package cart.infrastructure;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.entity.ProductEntity;
import cart.exception.ProductException;
import cart.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public JdbcProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        ProductEntity productEntity = productDao.getById(productId)
                .orElseThrow(() -> new ProductException.NotFound(productId));

        return Optional.of(ProductMapper.toProduct(productEntity));
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.getAll();
        return productEntities.stream()
                .map(ProductMapper::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Long create(Product product) {
        return productDao.insert(new ProductEntity(product.getName(), product.getPrice(), product.getImageUrl()));
    }

    @Override
    public void update(Product product) {
        productDao.update(new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()));
    }

    @Override
    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
