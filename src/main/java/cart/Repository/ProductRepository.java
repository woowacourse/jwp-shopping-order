package cart.Repository;

import cart.Repository.mapper.ProductMapper;
import cart.dao.ProductDao;
import cart.domain.Product.Product;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    private final ProductDao productDao;
    private final ProductMapper productMapper;

    public ProductRepository(ProductDao productDao, ProductMapper productMapper) {
        this.productDao = productDao;
        this.productMapper = productMapper;
    }


    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        return productMapper.toProducts(productEntities);
    }

    public Product getProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException(productId + "id 에 해당하는 상품을 찾을 수 없습니다."));

        return productMapper.toProduct(productEntity);
    }

    public Long createProduct(Product product) {
        ProductEntity productEntity = productMapper.toProductEntity(product);
        return productDao.createProduct(productEntity);
    }


    public void updateProduct(Long productId, Product product) {
        ProductEntity productEntity = productMapper.toProductEntity(product);
        productDao.updateProduct(productId, productEntity);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
