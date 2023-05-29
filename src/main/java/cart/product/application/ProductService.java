package cart.product.application;

import cart.product.application.dto.ProductRequest;
import cart.product.application.dto.ProductResponse;
import cart.product.dao.ProductDao;
import cart.product.domain.Product;
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
    List<Product> products = productDao.getAllProducts();
    return products.stream().map(ProductResponse::of).collect(Collectors.toList());
  }

  public ProductResponse getProductById(Long productId) {
    Product product = productDao.getProductById(productId);
    return ProductResponse.of(product);
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
