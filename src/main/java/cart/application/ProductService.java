package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        return productDao.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.findById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        return productDao.insert(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productDao.update(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.delete(productId);
    }
}