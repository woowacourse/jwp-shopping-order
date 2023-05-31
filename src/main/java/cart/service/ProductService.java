package cart.service;

import cart.entity.ProductEntity;
import cart.dao.ProductDao;
import cart.controller.dto.request.ProductRequest;
import cart.controller.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId);
        return ProductResponse.of(productEntity);
    }

    public Long createProduct(ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.createProduct(productEntity);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        ProductEntity productEntity = new ProductEntity(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, productEntity);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
