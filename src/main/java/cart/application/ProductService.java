package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> allProducts = productDao.getAllProducts();
        List<Product> products = allProducts.stream()
                .map(this::convertEntityToProduct)
                .collect(Collectors.toList());
        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        Product product = convertEntityToProduct(productEntity);
        return ProductResponse.from(product);
    }

    private Product convertEntityToProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl());
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
