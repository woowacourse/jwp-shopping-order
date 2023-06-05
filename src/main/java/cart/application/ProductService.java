package cart.application;

import cart.domain.product.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.NoSuchProductException;
import cart.persistence.dao.ProductDao;
import cart.util.ModelSortHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProducts() {
        List<Product> products = productDao.getAllProducts();
        ModelSortHelper.sortByIdInDescending(products);
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long productId) {
        Product product = productDao.findProductById(productId).orElseThrow(() -> new NoSuchProductException());
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
