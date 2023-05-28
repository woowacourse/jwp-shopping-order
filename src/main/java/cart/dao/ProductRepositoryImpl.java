package cart.dao;

import cart.domain.Product;
import cart.domain.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDao productDao;

    public ProductRepositoryImpl(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAllProducts() {
        final List<ProductEntity> allProducts = productDao.findAllProducts();

        return allProducts.stream()
                .map(ProductEntity::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product findProductById(final Long productId) {
        final ProductEntity product = productDao.findProductById(productId);

        return product.toProduct();
    }

    @Override
    public Long saveProduct(final Product product) {
        final ProductEntity productEntity = ProductEntity.from(product);

        return productDao.saveProduct(productEntity);
    }

    @Override
    public void updateProduct(final Long productId, final Product product) {
        final ProductEntity productEntity = ProductEntity.of(productId, product);

        productDao.updateProduct(productId, productEntity);
    }

    @Override
    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
