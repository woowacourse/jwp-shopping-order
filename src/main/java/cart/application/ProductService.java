package cart.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cart.domain.Product;
import cart.domain.ProductStock;
import cart.domain.Stock;
import cart.dto.ProductRequest;
import cart.dto.ProductStockResponse;
import cart.exception.ProductNotFoundException;
import cart.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductStockResponse createProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        final Stock stock = new Stock(productRequest.getStock());
        final ProductStock productStock = productRepository.createProduct(product, stock);
        return ProductStockResponse.of(productStock);
    }

    public List<ProductStockResponse> getAllProductStockResponses() {
        final List<ProductStock> productStocks = productRepository.getAllProductStocks();
        return productStocks.stream()
                .map(ProductStockResponse::of)
                .collect(Collectors.toList());
    }

    public ProductStockResponse getProductStockResponseById(final Long id) {
        final Optional<ProductStock> productStock = productRepository.getProductStockById(id);
        return ProductStockResponse.of(productStock.orElseThrow(() -> new ProductNotFoundException(id)));
    }

    public ProductStockResponse updateProduct(final Long id, final ProductRequest productRequest) {
        final Product product = new Product(id, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        final Stock stock = new Stock(productRequest.getStock());
        final Optional<ProductStock> productStock = productRepository.updateProductStock(product, stock);
        return ProductStockResponse.of(productStock.orElseThrow(() -> new ProductNotFoundException(id)));
    }

    public void deleteProduct(final Long id) {
        productRepository.deleteProductById(id);
    }
}
