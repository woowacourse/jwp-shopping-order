package cart.product.application;

import cart.product.application.dto.CreateProductCommand;
import cart.product.application.dto.UpdateProductCommand;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import cart.product.presentation.dto.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(CreateProductCommand command) {
        Product product = new Product(command.getName(), command.getPrice(), command.getImageUrl());
        return productRepository.save(product);
    }

    public void updateProduct(Long productId, UpdateProductCommand command) {
        Product product = new Product(productId, command.getName(), command.getPrice(), command.getImageUrl());
        productRepository.update(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.delete(productId);
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.from(product);
    }
}
