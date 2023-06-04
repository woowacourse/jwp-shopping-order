package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.domain.repository.ProductRepository;
import cart.entity.ProductEntity;
import cart.exception.ProductException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDao productDao;

    public ProductRepositoryImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Product findById(Long productId) {
        return toDomain(productDao.findById(productId)
                .orElseThrow(() -> new ProductException("잘못된 상품입니다.")));
    }

    @Override
    public Long save(Product product) {
        return productDao.save(product);
    }

    @Override
    public void updateById(Long productId, Product product) {
        productDao.updateById(productId, product);
    }

    @Override
    public void deleteById(Long productId) {
        productDao.deleteById(productId);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getImageUrl());
    }
}
