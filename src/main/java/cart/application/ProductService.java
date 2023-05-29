package cart.application;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Product getProductById(final Long productId) {
        return productDao.getProductById(productId);
    }

    @Transactional
    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.save(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl());
        productDao.update(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productDao.delete(productId);
    }
}
