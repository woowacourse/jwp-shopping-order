package cart.repository.product;

import cart.dao.policy.PolicyDao;
import cart.dao.product.ProductDao;
import cart.domain.product.Product;
import cart.entity.product.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final ProductDao productDao;
    private final PolicyDao policyDao;

    public ProductRepository(final ProductDao productDao, final PolicyDao policyDao) {
        this.productDao = productDao;
        this.policyDao = policyDao;
    }

    public Product findProductById(final long id) {
        ProductEntity productEntity = productDao.getProductById(id);
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
    }

    public List<Product> findAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();

        return productEntities.stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getImageUrl()))
                .collect(Collectors.toList());
    }

    public Long createProduct(final Product product) {
        long salePolicyId = policyDao.createProductDefaultPolicy();
        return productDao.createProduct(product, salePolicyId);
    }

    public void updateProduct(final Long productId, final Product product) {
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(final Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        productDao.deleteProduct(productId);
        policyDao.deletePolicy(productEntity.getPolicyId());
    }

    public void applySale(final long productId, final int amount) {
        ProductEntity productEntity = productDao.getProductById(productId);
        productDao.applySalePolicy(productId, true);
        policyDao.applySalePolicy(productEntity.getPolicyId(), amount);
    }

    public void unapplySale(final long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        productDao.applySalePolicy(productId, false);
        policyDao.applySalePolicy(productEntity.getPolicyId(), 0);
    }
}
