package cart.product.application;

import cart.product.application.dto.CreateProductCommand;
import cart.product.application.dto.UpdateProductCommand;
import cart.product.domain.Product;
import cart.product.infrastructure.persistence.dao.ProductDao;
import cart.product.presentation.dto.UpdateProductRequest;
import cart.product.presentation.dto.ProductResponse;
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
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(CreateProductCommand command) {
        Product product = new Product(command.getName(), command.getPrice(), command.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, UpdateProductCommand command) {
        Product product = new Product(command.getName(), command.getPrice(), command.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
