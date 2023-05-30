package cart.application;

import cart.application.dto.request.ProductRequest;
import cart.application.dto.response.ProductResponse;
import cart.domain.product.Product;
import cart.persistence.dao.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productDao.getAllProducts().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(final Long productId) {
        final Product product = productDao.getProductById(productId);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        return productDao.createProduct(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
        productDao.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
