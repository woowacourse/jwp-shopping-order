package cart.application;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dto.ProductRequest;
import cart.exception.notfound.ProductNotFoundException;
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
        return productDao.findAll();
    }

    public Product getProductById(final Long productId) {
        return productDao.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional
    public Long createProduct(final ProductRequest request) {
        final Product product = new Product(request.getName(), request.getPrice(), request.getImageUrl());
        return productDao.insert(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest request) {
        final Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getImageUrl());
        productDao.update(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productDao.delete(productId);
    }
}
