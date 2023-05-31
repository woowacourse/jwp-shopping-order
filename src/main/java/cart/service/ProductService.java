package cart.service;

import cart.domain.Product;
import cart.dao.ProductDao;
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
        List<Product> products = productDao.findAll();

        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.findById(productId);

        return ProductResponse.from(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl(), productRequest.getStock());

        return productDao.insert(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl(), productRequest.getStock());

        productDao.update(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
