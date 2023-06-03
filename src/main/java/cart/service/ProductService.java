package cart.service;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dto.PageRequest;
import cart.dto.PagingProductResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.paging.Paging;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public PagingProductResponse getAllProducts(final PageRequest pageRequest) {
        final Paging paging = new Paging(pageRequest);

        List<Product> products = productDao.getAllProductsBy(paging.getStart(), paging.getSize());
        final List<ProductResponse> productResponses = products.stream().map(ProductResponse::of).collect(Collectors.toList());

        final Integer count = productDao.countAllProduct();
        return new PagingProductResponse(paging.getPageInfo(count), productResponses);
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId);
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
