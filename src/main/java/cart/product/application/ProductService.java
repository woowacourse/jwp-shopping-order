package cart.product.application;

import cart.controller.product.dto.ProductRequest;
import cart.controller.product.dto.ProductResponse;
import cart.discountpolicy.DiscountPolicy;
import cart.product.Product;
import cart.sale.application.SaleService;
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

        for (DiscountPolicy discountPolicy : saleService.findDiscountPoliciesFromSales()) {
            products.forEach(product -> product.discount(discountPolicy));
        }

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.getProductById(productId);

        for (DiscountPolicy discountPolicy : saleService.findDiscountPoliciesFromSales()) {
            product.discount(discountPolicy);
        }

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
