package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.domain.vo.Amount;
import cart.ui.dto.request.ProductRequest;
import cart.ui.dto.response.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(final Long productId) {
        final Product product = productDao.getProductById(productId);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long createProduct(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), Amount.of(productRequest.getPrice()),
            productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    @Transactional
    public void updateProduct(final Long productId, final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), Amount.of(productRequest.getPrice()),
            productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
