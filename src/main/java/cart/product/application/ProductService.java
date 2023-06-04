package cart.product.application;

import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.product.Product;
import cart.controller.presentation.dto.ProductRequest;
import cart.controller.presentation.dto.ProductResponse;
import cart.sale.SaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SaleService saleService;

    public ProductService(ProductRepository productRepository, SaleService saleService) {
        this.productRepository = productRepository;
        this.saleService = saleService;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        for (Product product : products) {
            saleService.applySales(product, DiscountTarget.TOTAL);
        }
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.getProductById(productId);
        saleService.applySales(product);
        return ProductResponse.from(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }
}
