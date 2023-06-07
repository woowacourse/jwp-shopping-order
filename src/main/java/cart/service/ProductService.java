package cart.service;

import cart.dao.ProductDao;
import cart.domain.Price;
import cart.domain.Product;
import cart.dto.request.ProductAddRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public Product getProductById(Long productId) {
        return productDao.findById(productId);
    }

    public Long createProduct(ProductAddRequest productAddRequest) {
        Product product = new Product(productAddRequest.getName(), new Price(productAddRequest.getPrice()), productAddRequest.getImageUrl(), productAddRequest.getStock());

        return productDao.insert(product);
    }

    public void updateProduct(Long productId, ProductAddRequest productAddRequest) {
        Product newProduct = new Product(productId, productAddRequest.getName(), new Price(productAddRequest.getPrice()), productAddRequest.getImageUrl(), productAddRequest.getStock());

        productDao.update(newProduct);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteById(productId);
    }
}
