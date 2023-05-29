package cart.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.domain.ProductStock;
import cart.domain.Stock;
import cart.entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductStock createProduct(final Product product, final Stock stock) {
        final ProductEntity productEntity = new ProductEntity(product.getName(), product.getPrice(), product.getImageUrl(), stock.getValue());
        return productDao.insert(productEntity).toProductStock();
    }

    public List<ProductStock> getAllProductStocks() {
        final List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
                .map(ProductEntity::toProductStock)
                .collect(Collectors.toList());
    }

    public Optional<ProductStock> getProductStockById(final Long id) {
        return productDao.findById(id).map(ProductEntity::toProductStock);
    }

    public Optional<Product> getProductById(final Long id) {
        return productDao.findById(id).map(ProductEntity::toProduct);
    }

    public Optional<ProductStock> updateProductStock(final Product product, final Stock stock) {
        final ProductEntity productEntity = new ProductEntity(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), stock.getValue());
        return productDao.update(productEntity).map(ProductEntity::toProductStock);
    }

    public void deleteProductById(final Long id) {
        productDao.deleteById(id);
    }
}
