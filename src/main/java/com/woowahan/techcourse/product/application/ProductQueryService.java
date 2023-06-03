package com.woowahan.techcourse.product.application;

import com.woowahan.techcourse.product.dao.ProductDao;
import com.woowahan.techcourse.product.domain.Product;
import com.woowahan.techcourse.product.exception.ProductNotFoundException;
import com.woowahan.techcourse.product.ui.dto.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductDao productDao;

    public ProductQueryService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.of(product);
    }

    public Product findById(long productId) {
        return productDao.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
