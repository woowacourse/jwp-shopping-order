package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import cart.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryWithDao implements ProductRepository {

    private final ProductDao productDao;

    public ProductRepositoryWithDao(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long save(final Product product) {
        return productDao.save(toEntity(product));
    }

    public Product findById(final Long id) {
        return toDomain(productDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 상품이 없습니다.")));
    }

    public List<Product> findAll() {
        return productDao.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    public void update(final Product product) {
        productDao.update(toEntity(product));
    }

    public void deleteById(final Long id) {
        productDao.deleteById(id);
    }

    private Product toDomain(final ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    private ProductEntity toEntity(final Product product) {
        return new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
