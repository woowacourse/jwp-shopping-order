package cart.repository.product;

import cart.dao.policy.PolicyDao;
import cart.dao.product.ProductDao;
import cart.dao.product.ProductSaleDao;
import cart.domain.product.Product;
import cart.entity.product.ProductEntity;
import cart.entity.product.ProductSaleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final ProductDao productDao;
    private final PolicyDao policyDao;
    private final ProductSaleDao productSaleDao;

    public ProductRepository(final ProductDao productDao, final PolicyDao policyDao, final ProductSaleDao productSaleDao) {
        this.productDao = productDao;
        this.policyDao = policyDao;
        this.productSaleDao = productSaleDao;
    }

    public Product findProductById(final long id) {
        ProductEntity productEntity = productDao.findById(id);

        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.isOnSale(), productEntity.getSalePrice());
    }

    public List<Product> findAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();

        return productEntities.stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getImageUrl(), entity.isOnSale(), entity.getSalePrice()))
                .collect(Collectors.toList());
    }

    public Long createProduct(final Product product) {
        return productDao.createProduct(product);
    }

    public void updateProduct(final Long productId, final Product product) {
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }

    public void applySale(final long productId, final int salePrice, final int amount) {
        if (productSaleDao.isExistByProductId(productId)) {
            ProductSaleEntity productSaleEntity = productSaleDao.findByProductId(productId);
            policyDao.updateAmount(productSaleEntity.getPolicyId(), amount);
            productDao.applySalePolicy(productId, true);
            productDao.updateSaleAmount(productId, salePrice);
            return;
        }

        long policyId = policyDao.createProductSalePolicy(amount);
        productSaleDao.save(productId, policyId);
        productDao.applySalePolicy(productId, true);
        productDao.updateSaleAmount(productId, salePrice);
    }

    public void unapplySale(final long productId) {
        ProductSaleEntity productSaleEntity = productSaleDao.findByProductId(productId);

        productDao.applySalePolicy(productId, false);
        policyDao.deletePolicy(productSaleEntity.getPolicyId());
    }
}
