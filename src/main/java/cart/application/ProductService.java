package cart.application;

import cart.domain.Product;
import cart.dto.product.PagedProductsResponse;
import cart.dto.PaginationInfoDto;
import cart.repository.dao.ProductDao;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import java.util.Comparator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return ProductResponse.from(products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return ProductResponse.from(product);
    }

    @Transactional
    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }

    @Transactional(readOnly = true)
    public PagedProductsResponse getPagedProducts(final int unitSize, final int page) {
        final List<Product> allProducts = productDao.getAllProducts();

        int startOrder = unitSize * (page - 1);
        final List<Product> pagedProducts = allProducts.stream()
                .sorted(Comparator.comparingLong(Product::getId).reversed())
                .skip(startOrder)
                .limit(unitSize)
                .collect(Collectors.toUnmodifiableList());

        final List<ProductResponse> products = ProductResponse.from(pagedProducts);
        final int lastPage = (int) Math.ceil((double) allProducts.size() / unitSize);
        final PaginationInfoDto paginationInfo = new PaginationInfoDto(allProducts.size(), unitSize, page, lastPage);

        return new PagedProductsResponse(products, paginationInfo);
    }
}
