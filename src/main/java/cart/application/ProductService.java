package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public ProductService(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public List<ProductResponse> findAllProducts() {
        List<Product> products = productDao.findAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse findProductById(Long productId) {
        Product product = productDao.findProductById(productId);
        return ProductResponse.of(product);
    }

    public Long createProduct(final ProductRequest productRequest) {
        Product product = Product.of(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        Product product = Product.of(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
        cartItemDao.deleteByProductId(productId);
    }
}
