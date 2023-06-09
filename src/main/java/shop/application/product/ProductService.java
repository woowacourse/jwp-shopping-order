package shop.application.product;

import shop.application.product.dto.ProductDto;
import shop.application.product.dto.ProductModificationDto;

import java.util.List;

public interface ProductService {
    Long createProduct(ProductModificationDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long productId);

    void updateProduct(Long id, ProductModificationDto productDto);

    void deleteProduct(Long productId);
}
