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

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product save(final Product product) {
        final ProductEntity productEntity = productDao.save(
                new ProductEntity(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl(),
                        product.isDiscounted(),
                        product.getDiscountRate()
                )
        );
        return new Product(
                productEntity.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.isDiscounted(),
                product.getDiscountRate()
        );
    }

    public List<Product> findAll() {
        return productDao.findAll()
                .stream()
                .map(productEntity -> new Product(
                                productEntity.getId(),
                                productEntity.getName(),
                                productEntity.getPrice(),
                                productEntity.getImageUrl(),
                                productEntity.isDiscounted(),
                                productEntity.getDiscountRate()
                        )
                ).collect(Collectors.toList());
    }

    public Product findById(final Long id) {
        final ProductEntity productEntity = productDao.findById(id);
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.isDiscounted(),
                productEntity.getDiscountRate()
        );
    }

    public void updateById(final Long id, final Product product) {
        final ProductEntity productEntity = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.isDiscounted(),
                product.getDiscountRate()
        );
        productDao.updateById(id, productEntity);
    }

    public void deleteById(final Long id) {
        productDao.deleteById(id);
    }
}
