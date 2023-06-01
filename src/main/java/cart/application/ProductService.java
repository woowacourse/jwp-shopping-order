package cart.application;

import cart.dao.product.ProductDao;
import cart.domain.product.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.exception.customexception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.findAllProducts();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productDao.findProductById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getStock());
        return productDao.createProduct(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getStock());
        productDao.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
