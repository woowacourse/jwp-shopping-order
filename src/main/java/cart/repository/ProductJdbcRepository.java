package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Price;
import cart.domain.Product;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final ProductDao productDao;

    public ProductJdbcRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        final List<ProductEntity> productEntities = productDao.findAll();
        return mapToDomains(productEntities);
    }

    @Override
    public Product findById(final long id) {
        final ProductEntity productEntity = productDao.findById(id);
        return toDomain(productEntity);
    }

    @Override
    public List<Product> findByIds(final List<Long> ids) {
        return productDao.findByIds(ids).stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public long save(final Product product) {
        return productDao.insert(toEntity(product));
    }

    @Override
    public void update(final Product product) {
        productDao.update(toEntity(product));
    }

    @Override
    public void delete(final long id) {
        productDao.deleteById(id);
    }

    private List<Product> mapToDomains(final List<ProductEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    private Product toDomain(final ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), new Price(entity.getPrice()), entity.getImageUrl());
    }

    private ProductEntity toEntity(final Product product) {
        return new ProductEntity(product.getId(), product.getName(),
                product.getPrice().getAmount(), product.getImageUrl());
    }
}
