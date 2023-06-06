package cart.application;

import cart.domain.Product;
import cart.dao.ProductDao;
import cart.dto.request.ProductCreateRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ProductNotFoundException;
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
        List<Product> products = productDao.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품을 찾을 수 없습니다."));
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductCreateRequest productCreateRequest) {
        Product product = Product.builder()
                .name(productCreateRequest.getName())
                .price(productCreateRequest.getPrice())
                .imageUrl(productCreateRequest.getImageUrl())
                .build();
        return productDao.save(product);
    }

    public void updateProduct(Long productId, ProductCreateRequest productCreateRequest) {
        Product product = Product.builder()
                .name(productCreateRequest.getName())
                .price(productCreateRequest.getPrice())
                .imageUrl(productCreateRequest.getImageUrl())
                .build();
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
