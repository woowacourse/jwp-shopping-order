package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productDao.findAll();
        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toUnmodifiableList());
    }

    public ProductResponse findById(Long productId) {
        Product product = productDao.findById(productId);
        checkExistence(product);
        return ProductResponse.of(product);
    }

    @Transactional
    public Long add(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        return productDao.save(product);
    }

    @Transactional
    public void update(Long productId, ProductRequest productRequest) {
        Product product = productDao.findById(productId);
        checkExistence(product);

        Product newProduct = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        productDao.update(productId, newProduct);
    }

    private void checkExistence(Product product) {
        if (product == null) {
            throw new BadRequestException(ExceptionType.PRODUCT_NO_EXIST);
        }
    }

    public void remove(Long productId) {
        Product product = productDao.findById(productId);
        checkExistence(product);
        productDao.delete(productId);
    }
}
