package cart.product.repository;

import cart.cartitem.dao.CartItemDao;
import cart.product.dao.ProductDao;
import cart.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    
    public ProductRepository(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }
    
    public Product getProductById(final Long id) {
        return Product.from(productDao.getProductById(id));
    }
    
    public List<Product> getAllProducts() {
        return productDao.getAllProducts().stream()
                .map(Product::from)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public Long createProduct(final Product product) {
        final ProductEntity productEntity = new ProductEntity(
                null,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getPointRatio(),
                product.getPointAvailable()
        );
        return productDao.createProduct(productEntity);
    }
    
    public void updateProduct(final Product product) {
        final ProductEntity productEntity = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getPointRatio(),
                product.getPointAvailable()
        );
        productDao.updateProduct(productEntity);
    }
    
    public void removeProduct(final Long productId) {
        cartItemDao.deleteByProductId(productId);
        productDao.deleteProduct(productId);
    }
}
