package cart.service;

import cart.dao.ProductDao;
import cart.domain.Price;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Optional<Product> product = productDao.getProductById(productId);
        if (product.isPresent()) {
            return ProductResponse.of(product.get());
        }
        throw new ProductException("존재하지 않는 상품입니다");
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), new Price(productRequest.getPrice()), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), new Price(productRequest.getPrice()), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
