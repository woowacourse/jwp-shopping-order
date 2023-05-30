package cart.repository;

import static cart.exception.ErrorMessage.NOT_FOUND_PRODUCT;

import cart.domain.Product;
import cart.exception.ProductException;
import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long save(Product product) {
        ProductEntity productEntity = ProductEntity.createForSave(
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );

        return productDao.save(productEntity);
    }

    public void update(Long productId, Product product) {
        ProductEntity productEntity = new ProductEntity(
                productId,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                null,
                null
        );

        int updatedRow = productDao.update(productEntity);

        if (updatedRow == 0) {
            throw new ProductException(NOT_FOUND_PRODUCT);
        }
    }

    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.findAll();

        return productEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public Product findById(Long id) {
        ProductEntity productEntity = productDao.findById(id)
                .orElseThrow(() -> new ProductException(NOT_FOUND_PRODUCT));

        return toDomain(productEntity);
    }

    public void deleteById(Long id) {
        int deletedRow = productDao.deleteId(id);

        if (deletedRow == 0) {
            throw new ProductException(NOT_FOUND_PRODUCT);
        }
    }

    private Product toDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl()
        );
    }
}
