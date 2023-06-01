package cart.service;

import cart.domain.Product;
import cart.dao.ProductDao;
import cart.dto.ProductAddRequest;
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

    public Long createProduct(ProductAddRequest productAddRequest) {
        Product product = new Product(productAddRequest.getName(), productAddRequest.getPrice(), productAddRequest.getImageUrl(), productAddRequest.getStock());

        return productDao.insert(product);
    }

    public void updateProduct(Long productId, ProductAddRequest productAddRequest) {
        Product product = new Product(productAddRequest.getName(), productAddRequest.getPrice(), productAddRequest.getImageUrl(), productAddRequest.getStock());

        productDao.update(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
