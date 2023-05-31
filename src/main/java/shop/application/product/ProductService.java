package shop.application.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.product.dto.ProductDto;
import shop.application.product.dto.ProductModificationDto;
import shop.domain.product.Product;
import shop.domain.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductDto.of(product);
    }

    @Transactional
    public Long createProduct(ProductModificationDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );

        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long id, ProductModificationDto productDto) {
        Product product = new Product(
                id,
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );

        productRepository.update(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
