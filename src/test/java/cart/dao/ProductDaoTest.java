package cart.dao;

import cart.dao.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class ProductDaoTest {

    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @Autowired
    ProductDaoTest(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    public void 저장한다() {
        ProductEntity productEntity = new ProductEntity(null, "새로운 상품", 500, "새상품 이미지");

        Long savedProductId = productDao.save(productEntity);

        assertThat(savedProductId).isNotNull();
    }

    @Test
    public void 모두_조회한다() {
        List<ProductEntity> products = productDao.findAll();

        assertThat(products).hasSize(12);
    }

    @Test
    public void 아이디로_조회한다() {
        Long id = 1L;

        ProductEntity product = productDao.findById(id).get();

        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    public void 아이디로_조회_시_없으면_빈_값_반환한다() {
        Long id = 100L;

        Optional<ProductEntity> product = productDao.findById(id);

        assertThat(product).isEmpty();
    }

    @Test
    public void 수정한다() {
        Long id = 1L;
        ProductEntity productEntity = new ProductEntity(id, "수정", 999, "수정된 주소");

        productDao.update(productEntity);

        ProductEntity updatedProduct = productDao.findById(id).get();
        assertAll(
            () -> assertThat(productEntity.getName()).isEqualTo(updatedProduct.getName()),
            () -> assertThat(productEntity.getPrice()).isEqualTo(updatedProduct.getPrice()),
            () -> assertThat(productEntity.getImageUrl()).isEqualTo(updatedProduct.getImageUrl())
        );
    }

    @Test
    public void 삭제한다() {
        // Given
        ProductEntity productEntity = new ProductEntity(null, "새로운 상품", 500, "새상품 이미지");
        Long id = productDao.save(productEntity);

        // When
        productDao.deleteById(id);

        // Then
        Optional<ProductEntity> deletedProduct = productDao.findById(id);
        assertThat(deletedProduct).isEmpty();
    }
}
