package cart.application;

import cart.dao.dto.PageInfo;
import cart.domain.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.product.ProductsResponse;
import cart.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductsResponse getProducts(int size, int page) {
        PageInfo pageInfo = productRepository.findPageInfo(size, page);
        List<Product> products = productRepository.findProductsByPage(size, page);
        return ProductsResponse.of(products, pageInfo);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> allProducts = productRepository.getAllProducts();
        return allProducts.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findProductById(productId);
        return ProductResponse.from(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());

        return productRepository.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productId, productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());

        productRepository.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }
}
