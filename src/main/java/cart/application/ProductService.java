package cart.application;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productDao.getAllProducts();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        final Product product = productDao.getProductById(productId);

        return ProductResponse.from(product);
    }

    @Transactional
    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());

        return productDao.createProduct(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());

        productDao.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
