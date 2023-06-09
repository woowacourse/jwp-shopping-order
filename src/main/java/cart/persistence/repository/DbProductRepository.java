package cart.persistence.repository;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.exception.NoSuchProductException;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.ProductEntity;
import cart.persistence.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbProductRepository implements ProductRepository {
    private final ProductDao productDao;

    public DbProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll().stream()
                .map(ProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        ProductEntity productEntity = productDao.findById(id).orElseThrow(() -> new NoSuchProductException());
        return ProductMapper.toDomain(productEntity);
    }

    @Override
    public Long add(Product product) {
        return productDao.add(ProductMapper.toEntity(product));
    }

    @Override
    public void update(Long productId, Product product) {
        productDao.update(productId, ProductMapper.toEntity(product));
    }

    @Override
    public void delete(Long productId) {
        productDao.delete(productId);
    }
}
