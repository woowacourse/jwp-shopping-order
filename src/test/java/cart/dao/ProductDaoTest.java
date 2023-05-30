package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
        jdbcTemplate.update("DELETE FROM product");
    }

    @Test
    void 상품을_저장한다() {
        // given
        ProductEntity product = new ProductEntity("밀리", BigDecimal.valueOf(1_000_000_000), "http://millie.com");

        // when
        Long id = productDao.save(product);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 상품을_id로_조회한다() {
        // given
        ProductEntity product = new ProductEntity("밀리", BigDecimal.valueOf(1_000_000_000), "http://millie.com");
        Long id = productDao.save(product);

        // when
        Optional<ProductEntity> savedProduct = productDao.findById(id);

        // then
        assertThat(savedProduct).isPresent();
    }

    @Test
    void 상품을_수정한다() {
        // given
        ProductEntity product = new ProductEntity("밀리", BigDecimal.valueOf(1_000_000_000), "http://millie.com");
        Long id = productDao.save(product);

        // when
        ProductEntity newProduct = new ProductEntity(id, "박스터", BigDecimal.valueOf(10), "http://boxster.com");
        productDao.updateProduct(newProduct);

        // then
        ProductEntity updatedProduct = productDao.findById(id).get();
        assertThat(updatedProduct).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(newProduct);
    }

    @Test
    void 상품을_전체_조회한다() {
        // given
        productDao.save(new ProductEntity("밀리", BigDecimal.valueOf(1_000_000_000), "http://millie.com"));
        productDao.save(new ProductEntity("박스터", BigDecimal.valueOf(10), "http://boxster.com"));

        // when
        List<ProductEntity> productEntities = productDao.findAll();

        // then
        assertThat(productEntities).hasSize(2);
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Long id = productDao.save(new ProductEntity("밀리", BigDecimal.valueOf(1_000_000_000), "http://millie.com"));

        // when
        productDao.deleteProduct(id);

        // then
        Optional<ProductEntity> productEntity = productDao.findById(id);
        assertThat(productEntity).isEmpty();
    }
}
