package shop.application.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.application.product.dto.ProductDto;
import shop.application.product.dto.ProductModificationDto;
import shop.domain.product.Product;
import shop.domain.repository.ProductRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public Long createProduct(ProductModificationDto productDto) {
        Product product = new Product(
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );

        return productRepository.save(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return ProductDto.of(products);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductDto.of(product);
    }

    @Transactional
    @Override
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
    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
