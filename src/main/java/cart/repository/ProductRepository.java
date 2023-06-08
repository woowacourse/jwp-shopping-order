package cart.repository;

import cart.dao.ProductDao;
import cart.dao.dto.product.ProductDto;
import cart.domain.Product;
import cart.exception.notfoundexception.ProductNotFoundException;
import cart.repository.mapper.ProductMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        List<ProductDto> productEntities = productDao.findAll();
        return productEntities.stream()
            .map(ProductMapper::toProduct)
            .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        ProductDto productDto = productDao.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        return ProductMapper.toProduct(productDto);
    }

    public long save(Product product) {
        return productDao.save(ProductMapper.toProductDto(product));
    }


    public void updateProduct(Long productId, Product product) {
        validateProductExistence(productId);
        productDao.updateProduct(productId, ProductMapper.toProductDto(product));
    }

    public void deleteById(long productId) {
        validateProductExistence(productId);
        productDao.deleteById(productId);
    }

    private void validateProductExistence(long productId) {
        if (productDao.isNonExistingId(productId)) {
            throw new ProductNotFoundException(productId);
        }
    }
}
