package cart.application;

import static cart.application.mapper.ProductMapper.convertProduct;
import static cart.application.mapper.ProductMapper.convertProductResponse;

import cart.application.dto.product.ProductPageResponse;
import cart.application.dto.product.ProductRequest;
import cart.application.dto.product.ProductResponse;
import cart.application.mapper.ProductMapper;
import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.domain.product.ProductWithId;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        final List<ProductWithId> products = productRepository.getAllProducts();
        return products.stream()
            .map(ProductMapper::convertProductResponse)
            .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        final Product product = productRepository.getProductById(productId);
        return convertProductResponse(productId, product);
    }

    public ProductPageResponse getProductsByPage(final int page, final int size) {
        final List<ProductWithId> products = productRepository.getProductsByPage(page, size);
        final List<ProductResponse> productResponses = products.stream()
            .map(ProductMapper::convertProductResponse)
            .collect(Collectors.toList());

        final long totalProductCount = productRepository.getAllProductCount();
        final long totalPage = calculateTotalPage(size, totalProductCount);
        return new ProductPageResponse(totalPage, productResponses);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        final Product product = convertProduct(productRequest);
        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        final Product product = convertProduct(productRequest);
        productRepository.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }

    private long calculateTotalPage(final int size, final long totalProductCount) {
        long totalPage = totalProductCount / size;

        if (totalProductCount % size > 0) {
            totalPage++;
        }
        return totalPage;
    }
}
