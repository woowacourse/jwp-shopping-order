package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ProductException.DuplicatedProduct;
import cart.exception.ProductException.InvalidProduct;
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
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId)
                .orElseThrow(InvalidProduct::new);
        return ProductResponse.from(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        if (productDao.countByProduct(product) != 0) {
            throw new DuplicatedProduct();
        }
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        if (productDao.countByProduct(product) != 0) {
            throw new DuplicatedProduct();
        }
        int updatedProductCount = productDao.updateProduct(productId, product);
        if (updatedProductCount == 0) {
            throw new InvalidProduct();
        }
    }

    public void deleteProduct(Long productId) {
        int deletedProductCount = productDao.deleteProduct(productId);
        if (deletedProductCount == 0) {
            throw new InvalidProduct();
        }
    }
}
