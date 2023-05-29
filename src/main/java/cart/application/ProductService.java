package cart.application;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.getAllProducts();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId);

        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = toProduct(productRequest);

        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = toProduct(productRequest);

        productDao.updateProduct(productId, product);
    }

    private Product toProduct(ProductRequest productRequest) {
        return new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl()
        );
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
