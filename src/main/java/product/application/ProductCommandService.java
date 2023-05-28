package product.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.application.dto.ProductRequest;
import product.dao.ProductDao;
import product.domain.Product;

@Service
@Transactional
public class ProductCommandService {

    private final ProductDao productDao;

    public ProductCommandService(ProductDao productDao) {
        this.productDao = productDao;
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
