package cart.repository.product;

import cart.dao.product.ProductDao;
import cart.domain.product.Product;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream()
                .map(this::makeProduct)
                .collect(Collectors.toUnmodifiableList());
    }

    public Product findById(final Long id) {
        ProductEntity productEntity = productDao.getProductById(id).orElseThrow();
        return makeProduct(productEntity);
    }

    public Long create(final Product product) {
        ProductEntity productEntity = makeProductEntity(product);
        return productDao.createProduct(productEntity);
    }

    public void update(final Long id, final Product updateProduct) {
        productDao.updateProduct(id, makeProductEntity(updateProduct));
    }

    public void delete(final Long id) {
        productDao.deleteProduct(id);
    }

    private Product makeProduct(final ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.isDiscounted(),
                productEntity.getDiscountRate());
    }

    private ProductEntity makeProductEntity(final Product product) {
        return new ProductEntity(
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.isDiscounted(),
                product.getDiscountRate());
    }
}
