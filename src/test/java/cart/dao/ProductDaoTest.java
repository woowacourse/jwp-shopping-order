package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 상품을_저장한다() {
        // given
        final ProductEntity product = new ProductEntity("허브티", "tea.jpg", 1000L);

        // when
        productDao.insert(product);

        // then
        assertThat(productDao.findAll()).hasSize(1);
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        final ProductEntity product1 = new ProductEntity("허브티", "tea.jpg", 1000L);
        final ProductEntity product2 = new ProductEntity("고양이", "cat.jpg", 1000000L);
        final ProductEntity savedProduct1 = productDao.insert(product1);
        final ProductEntity savedProduct2 = productDao.insert(product2);

        // when
        final List<ProductEntity> result = productDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(List.of(savedProduct1, savedProduct2));
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        final ProductEntity product = new ProductEntity("허브티", "tea.jpg", 1000L);
        final ProductEntity savedProduct = productDao.insert(product);

        // when
        final ProductEntity result = productDao.findById(savedProduct.getId()).get();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(savedProduct);
    }

    @Test
    void 상품을_수정한다() {
        // given
        final ProductEntity product = new ProductEntity("허브티", "tea.jpg", 1000L);
        final ProductEntity savedProduct = productDao.insert(product);
        final ProductEntity updatedProduct = new ProductEntity(savedProduct.getId(), "블랙캣", "cat.jpg", 10000L);

        // when
        productDao.update(updatedProduct);

        // then
        final ProductEntity result = productDao.findById(updatedProduct.getId()).get();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("블랙캣"),
                () -> assertThat(result.getImageUrl()).isEqualTo("cat.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(10000L)
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        final ProductEntity product = new ProductEntity("허브티", "tea.jpg", 1000L);
        final ProductEntity savedProduct = productDao.insert(product);

        // when
        productDao.deleteById(savedProduct.getId());

        // then
        assertThat(productDao.findAll()).isEmpty();
    }

    @Test
    void 입력받은_아이디에_대한_상품을_조회한다() {
        // given
        final ProductEntity product1 = new ProductEntity("허브티", "tea.jpg", 1000L);
        final ProductEntity product2 = new ProductEntity("고양이", "cat.jpg", 1000000L);
        final ProductEntity product3 = new ProductEntity("스프라이트", "sprite.jpg", 1000000L);
        final ProductEntity savedProduct1 = productDao.insert(product1);
        final ProductEntity savedProduct2 = productDao.insert(product2);
        final ProductEntity savedProduct3 = productDao.insert(product3);

        // when
        final List<ProductEntity> result = productDao.findByIds(List.of(savedProduct1.getId(), savedProduct2.getId()));

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(List.of(savedProduct1, savedProduct2));
    }
}
