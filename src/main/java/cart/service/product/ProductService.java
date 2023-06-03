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
    public List<ProductResponse> findAllProducts() {
        return productRepository.findAllProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(final Long productId) {
        return ProductResponse.from(productRepository.findProductById(productId));
    }

    @Transactional
    public long createProduct(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getProductName(), productRequest.getProductPrice(), productRequest.getImageUrl());
        return productRepository.createProduct(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        Product product = new Product(productRequest.getProductName(), productRequest.getProductPrice(), productRequest.getImageUrl());
        productRepository.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productRepository.deleteProduct(productId);
    }

    @Transactional
    public long applySale(final Long productId, final SaleProductRequest request) {
        Product product = productRepository.findProductById(productId);
        int salePrice = product.applySale(request.getAmount());

        productRepository.applySale(productId, salePrice, request.getAmount());
        return productId;
    }

    @Transactional
    public long unapplySale(final Long productId) {
        Product product = productRepository.findProductById(productId);
        productRepository.unapplySale(productId);
        return product.getId();
    }
}
