package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse findById(Long productId) {
        Product product = productDao.getProductById(productId);
        return ProductResponse.of(product);
    }

    public Long add(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void update(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void remove(Long productId) {
        productDao.deleteProduct(productId);
    }
}
