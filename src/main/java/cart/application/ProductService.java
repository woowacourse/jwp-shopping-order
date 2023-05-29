package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.NoSuchDataExistException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> findAllProducts() {
        final List<Product> products = productDao.findAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse findProductById(final Long productId) {
        final Product product = productDao.findProductById(productId)
                .orElseThrow(NoSuchDataExistException::new);

        return ProductResponse.of(product);
    }

    public List<ProductResponse> findProductById(final List<Long> productIds) {
        final List<Product> products = productDao.findByIds(productIds);

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long saveProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        return productDao.saveProduct(product);
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
