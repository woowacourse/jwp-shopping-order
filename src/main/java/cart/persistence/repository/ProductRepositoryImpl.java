package cart.persistence.repository;

import static cart.persistence.mapper.ProductMapper.convertProduct;
import static cart.persistence.mapper.ProductMapper.convertProductEntity;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.domain.product.ProductWithId;
import cart.exception.DBException;
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
        return productDao.getNotDeletedProducts().stream()
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
    public List<ProductWithId> getProductsByPage(final int page, final int size) {
        return productDao.getProductsByPage(page, size).stream()
            .map(ProductMapper::convertProductWithId)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public long getAllProductCount() {
        return productDao.getAllProductCount();
    }

    @Override
    public Long save(final Product product) {
        final ProductEntity productEntity = convertProductEntity(product);
        return productDao.insert(productEntity);
    }

    @Override
    public void updateProduct(final Long productId, final Product product) {
        final ProductEntity productEntity = convertProductEntity(product);
        final int updatedCount = productDao.updateProduct(productId, productEntity);
        if (updatedCount != 1) {
            throw new DBException(ErrorCode.DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteProduct(final Long productId) {
        final int deletedCount = productDao.updateProductDeleted(productId);
        if (deletedCount != 1) {
            throw new DBException(ErrorCode.DB_DELETE_ERROR);
        }
    }

    @Override
    public boolean existById(final Long id) {
        return productDao.existById(id);
    }
}
