package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
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
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        ProductEntity productEntity = productDao.findById(productId);
        return convertToDomain(productEntity);
    }

    public Long save(Product product) {
        ProductEntity productEntity = convertToEntity(product);
        return productDao.create(productEntity);
    }

    public void update(Product product) {
        ProductEntity productEntity = convertToEntity(product);
        productDao.update(productEntity);
    }

    private Product convertToDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl()
        );
    }

    private ProductEntity convertToEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
    }
}
