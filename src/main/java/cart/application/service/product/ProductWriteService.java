package cart.application.service.product;

import cart.application.repository.product.ProductRepository;
import cart.application.service.product.dto.ProductCommandDto;
import cart.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductWriteService {

    private final ProductRepository productRepository;

    public ProductWriteService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Long createProduct(final ProductCommandDto productCommandDto) {
        final Product product = new Product(productCommandDto);
        return productRepository.createProduct(product);
    }

    public void updateProduct(final Long productId, final ProductCommandDto productCommandDto) {
        productRepository.getById(productId);

        final Product product = new Product(productId, productCommandDto);
        productRepository.updateProduct(product);
    }

    public void deleteProduct(final Long productId) {
        productRepository.deleteProduct(productId);
    }

}
