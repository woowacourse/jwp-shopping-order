package cart.service.product;

import cart.domain.product.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.sale.SaleProductRequest;
import cart.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(final Long productId) {
        return ProductResponse.from(productRepository.findProductById(productId));
    }

    @Transactional
    public Long createProduct(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.createProduct(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.deleteProduct(productId);
    }

    @Transactional
    public long applySale(final Long productId, final SaleProductRequest request) {
        Product product = productRepository.findProductById(productId);
        product.applySale(request.getAmount());

        productRepository.applySale(productId, request.getAmount());
        return productId;
    }

    @Transactional
    public long unapplySale(final Long productId) {
        Product product = productRepository.findProductById(productId);
        product.unApplySale();

        productRepository.unapplySale(productId);
        return product.getId();
    }
}
