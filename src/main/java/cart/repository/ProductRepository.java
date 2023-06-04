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

    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
                .map(ProductEntity::toDomain)
                .collect(toList());
    }

    public Optional<Product> findById(Long productId) {
        return productDao.findById(productId)
                .map(ProductEntity::toDomain);
    }

    public Product save(Product product) {
        Long id = productDao.save(toEntity(product));
        return new Product(id, product.getName(), product.getPrice(), product.getImageUrl());
    }

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

    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }

}
