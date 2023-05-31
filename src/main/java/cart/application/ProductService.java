package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
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
        //todo : 존재하지 않는 productId 예외처리
        Product product = productDao.getProductById(productId);
        return ProductResponse.of(product);
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
            throw new IllegalProduct();
        }
    }

    // todo: delete할 때 cartItem은 삭제 안해주고 있네
    public void deleteProduct(Long productId) {
        int deletedProductCount = productDao.deleteProduct(productId);
        if (deletedProductCount == 0) {
            throw new IllegalProduct();
        }
    }
}
