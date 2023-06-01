package cart.repository;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.ProductDao;
import cart.dao.dto.ProductDto;
import cart.domain.product.Product;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        return productDao.getAllProducts().stream()
                .map(ProductDto::toProduct)
                .collect(toUnmodifiableList());
    }

    public Product findById(final Long productId) {
        return productDao.getProductById(productId).toProduct();
    }

    public Product insertProduct(final Product product) {
        final Long productId = productDao.createProduct(ProductDto.from(product));
        return new Product(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    public void updateProduct(final Long productId, final Product product) {
        productDao.updateProduct(productId, ProductDto.from(product));
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
