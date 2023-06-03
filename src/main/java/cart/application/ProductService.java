package cart.application;

import cart.domain.Amount;
import cart.domain.Product;
import cart.dao.ProductDao;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.BusinessException;
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
        Product product = productDao.getProductById(productId)
            .orElseThrow(() -> new BusinessException("찾는 상품이 존재하지 않습니다."));
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), new Amount(productRequest.getPrice()), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), new Amount(productRequest.getPrice()), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
