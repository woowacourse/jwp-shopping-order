package cart.application.service.product;

import cart.application.repository.ProductRepository;
import cart.application.service.product.dto.ProductCreateDto;
import cart.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductWriteService {

    private final ProductRepository productRepository;

    public ProductWriteService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(ProductCreateDto productCreateDto) {
        Product product = new Product(productCreateDto.getName(), productCreateDto.getPrice(), productCreateDto.getImageUrl());
        return productRepository.createProduct(product);
    }

    public void updateProduct(Long productId, ProductCreateDto productCreateDto) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));

        Product product = new Product(productId, productCreateDto.getName(), productCreateDto.getPrice(), productCreateDto.getImageUrl());
        productRepository.updateProduct(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
    }

}
