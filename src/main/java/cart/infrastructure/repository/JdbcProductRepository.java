package cart.infrastructure.repository;

import static java.util.stream.Collectors.toList;

import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.infrastructure.dao.ProductDao;
import cart.infrastructure.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public JdbcProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
                .map(ProductEntity::toDomain)
                .collect(toList());
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productDao.findById(productId)
                .map(ProductEntity::toDomain);
    }

    @Override
    public Product save(Product product) {
        Long id = productDao.save(toEntity(product));
        return new Product(id, product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Override
    public void update(Product product) {
        ProductEntity productEntity = toEntity(product);
        productDao.update(productEntity);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice().getValue(),
                product.getImageUrl()
        );
    }

    @Override
    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
