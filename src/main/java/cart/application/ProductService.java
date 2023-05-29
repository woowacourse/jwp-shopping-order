package cart.application;

import cart.domain.Product;
import cart.dao.ProductDao;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductException;
import cart.exception.ProductException.DuplicatedProduct;
import cart.exception.ProductException.IllegalProduct;
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
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        if(productDao.countByProduct(product) != 0){
            throw new DuplicatedProduct();
        }
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        if(productDao.countByProduct(product) != 0){
            throw new DuplicatedProduct();
        }
        int updatedProductCount = productDao.updateProduct(productId, product);
        if(updatedProductCount == 0) {
            throw new IllegalProduct();
        }
    }

    public void deleteProduct(Long productId) {
        int deletedProductCount = productDao.deleteProduct(productId);
        if(deletedProductCount == 0) {
            throw new IllegalProduct();
        }
    }
}
