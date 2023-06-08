package cart.product.application;

import cart.product.ui.dto.ProductRequest;
import cart.product.ui.dto.ProductDto;
import cart.common.exception.notFound.ProductNotFoundException;
import cart.product.domain.Product;
import cart.product.persistence.ProductDao;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductDto::of).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Product product = productDao.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductDto.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl(),
                productRequest.getStock());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl(),
                productRequest.getStock());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
