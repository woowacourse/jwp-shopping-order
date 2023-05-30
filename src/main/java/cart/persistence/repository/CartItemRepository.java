package cart.persistence.repository;

import cart.domain.product.Product;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import static cart.persistence.repository.Mapper.productEntityToProductMapper;

@Component
public class CartItemRepository {

    private final ProductDao productDao;

    public CartItemRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product getProductById(final Long productId) {
        final ProductEntity productEntity = productDao.getProductById(productId);
        return productEntityToProductMapper(productEntity);
    }
}
