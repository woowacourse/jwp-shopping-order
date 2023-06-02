package shop.persistence;

import shop.domain.product.Product;
import shop.domain.repository.ProductRepository;
import shop.exception.DatabaseException;
import shop.persistence.dao.ProductDao;
import shop.persistence.entity.ProductEntity;
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
    public Long save(Product product) {
        ProductEntity productEntity =
                toEntity(product);

        return productDao.insert(productEntity);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(product.getName(), product.getPrice(), product.getImageUrl());
    }

    @Override
    public List<Product> findAll() {
        List<ProductEntity> allProductEntities = productDao.findAll();

        return allProductEntities.stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        ProductEntity productEntity = productDao.findById(id)
                .orElseThrow(() -> new DatabaseException.IllegalDataException(id + "에 해당하는 상품이 없습니다."));

        return toProduct(productEntity);
    }

    @Override
    public void update(Product product) {
        int countOfUpdatedProduct = productDao.updateProduct(product.getId(), toEntity(product));

        if (countOfUpdatedProduct == 0) {
            throw new DatabaseException.IllegalDataException(product.getId() + "에 해당하는 상품이 없습니다.");
        }
    }

    private Product toProduct(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl()
        );
    }

    @Override
    public void deleteById(Long id) {
        productDao.deleteProduct(id);
    }
}
