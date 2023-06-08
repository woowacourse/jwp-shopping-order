package cart.application;

import cart.domain.Product;
import cart.dto.PagedDataResponse;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.repository.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        final Pageable allProductsSortedByIdDesc = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id").descending());
        final Page<Product> products = productRepository.findAll(allProductsSortedByIdDesc);
        return ProductResponse.from(products.getContent());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId);
        return ProductResponse.from(product);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productRepository.update(productId, product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional(readOnly = true)
    public PagedDataResponse<ProductResponse> getPagedProducts(final int unitSize, final int page) {
        final Pageable sortedByIdDesc = PageRequest.of(page - 1, unitSize, Sort.by("id").descending());
        final Page<Product> pagedProducts = productRepository.findAll(sortedByIdDesc);

        final Page<ProductResponse> response = pagedProducts.map(product -> ProductResponse.from(product));
        return PagedDataResponse.from(response);
    }
}
