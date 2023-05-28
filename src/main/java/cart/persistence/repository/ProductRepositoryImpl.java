package cart.persistence.repository;

import static cart.persistence.mapper.ProductMapper.convertProduct;
import static cart.persistence.mapper.ProductMapper.convertProductEntity;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.domain.product.dto.ProductWithId;
import cart.exception.ErrorCode;
import cart.exception.NotFoundException;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.ProductEntity;
import cart.persistence.mapper.ProductMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductDao productDao;

    public ProductRepositoryImpl(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<ProductWithId> getAllProducts() {
        return productDao.getAllProducts().stream()
            .map(ProductMapper::convertProductWithId)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Product getProductById(final Long productId) {
        final ProductEntity productEntity = productDao.getProductById(productId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        return convertProduct(productEntity);
    }

    @Override
    public Long save(final Product product) {
        final ProductEntity productEntity = convertProductEntity(product);
        return productDao.insert(productEntity);
    }

    @Override
    public void updateProduct(final Long productId, final Product product) {
        final ProductEntity productEntity = convertProductEntity(product);
        productDao.updateProduct(productId, productEntity);
    }

    @Override
    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
