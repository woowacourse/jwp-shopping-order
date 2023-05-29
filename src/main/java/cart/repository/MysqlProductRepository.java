package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MysqlProductRepository implements ProductRepository {

    private final ProductDao productDao;

    public MysqlProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        final List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(final long id) {
        final ProductEntity productEntity = productDao.findById(id);
        return toProduct(productEntity);
    }

    @Override
    public Product save(final Product product) {
        final ProductEntity productEntity = new ProductEntity(product.getName(), product.getPrice(), product.getImageUrl());
        final long id = productDao.createProduct(productEntity);
        return new Product(id, productEntity.getName(), product.getPrice(), product.getImageUrl());
    }

    @Override
    public void updateById(final long id, final Product product) {
        final ProductEntity productEntity = new ProductEntity(id, product.getName(), product.getPrice(), product.getImageUrl());
        productDao.updateProduct(id, productEntity);
    }

    @Override
    public void deleteById(final long id) {
        productDao.deleteProduct(id);
    }

    private Product toProduct(final ProductEntity productEntity) {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl());
    }

}
