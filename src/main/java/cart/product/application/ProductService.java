package cart.product.application;

import cart.product.application.dto.ProductAddDto;
import cart.product.application.dto.ProductUpdateDto;
import cart.product.application.dto.ProductDto;
import cart.common.exception.notFound.ProductNotFoundException;
import cart.product.domain.Product;
import cart.product.persistence.ProductDao;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductDto::of).collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Product product = productDao.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductDto.of(product);
    }

    public Long createProduct(ProductAddDto productAddDto) {
        Product product = new Product(productAddDto.getName(), productAddDto.getPrice(), productAddDto.getImageUrl(),
                productAddDto.getStock());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductUpdateDto productUpdateDto) {
        Product product = new Product(productUpdateDto.getName(), productUpdateDto.getPrice(), productUpdateDto.getImageUrl(),
                productUpdateDto.getStock());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
