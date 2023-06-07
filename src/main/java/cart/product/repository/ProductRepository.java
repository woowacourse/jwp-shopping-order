package cart.product.repository;

import cart.cartitem.dao.CartItemDao;
import cart.productpoint.dao.ProductPointDao;
import cart.productpoint.repository.ProductPointEntity;
import cart.product.dao.ProductDao;
import cart.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final ProductPointDao productPointDao;
    
    public ProductRepository(final ProductDao productDao, final CartItemDao cartItemDao, final ProductPointDao productPointDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.productPointDao = productPointDao;
    }
    
    public Product getProductById(final Long id) {
        final ProductEntity productEntity = productDao.getProductById(id);
        return Product.of(productEntity, productPointDao.getProductPointById(productEntity.getProductPointId()));
    }
    
    public List<Product> getAllProducts() {
        return productDao.getAllProducts().stream()
                .map(productEntity -> {
                    final ProductPointEntity pointById = productPointDao.getProductPointById(productEntity.getProductPointId());
                    return Product.of(productEntity, pointById);
                })
                .collect(Collectors.toUnmodifiableList());
    }
    
    public Long createProduct(final Product product) {
        final ProductPointEntity productPointEntity = ProductPointEntity.from(product.getPoint());
        final Long pointId = productPointDao.createProductPoint(productPointEntity);
        final ProductEntity productEntity = new ProductEntity(
                null,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                pointId
        );
        return productDao.createProduct(productEntity);
    }
    
    public void updateProduct(final Product product) {
        final Long productId = product.getId();
        final ProductEntity productEntityById = productDao.getProductById(productId);
        final Long pointId = productEntityById.getProductPointId();
        final ProductPointEntity pointEntityByProductPointId = productPointDao.getProductPointById(pointId);
        productPointDao.update(pointEntityByProductPointId);
        
        final ProductEntity productEntity = new ProductEntity(
                productId,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                pointId
        );
        productDao.updateProduct(productEntity);
    }
    
    public void removeProduct(final Long productId) {
        cartItemDao.deleteByProductId(productId);
        final ProductEntity productEntity = productDao.getProductById(productId);
        productPointDao.deleteById(productEntity.getProductPointId());
        productDao.deleteById(productId);
    }
}
