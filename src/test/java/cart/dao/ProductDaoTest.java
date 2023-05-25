package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import java.time.LocalDateTime;
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
        final Product product = new Product("허브티", "tea.jpg", 1000L);

        // when
        final Product savedProduct = productDao.save(product);

        // then
        final List<Product> products = productDao.findAll();
        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(savedProduct).isEqualTo(products.get(0))
        );
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        final Product product1 = new Product("허브티", "tea.jpg", 1000L);
        final Product product2 = new Product("고양이", "cat.jpg", 1000000L);
        final Product savedProduct1 = productDao.save(product1);
        final Product savedProduct2 = productDao.save(product2);

        // when
        List<Product> result = productDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(List.of(savedProduct1, savedProduct2));
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        final Product product = new Product("허브티", "tea.jpg", 1000L);
        final Product savedProduct = productDao.save(product);

        // when
        final Product result = productDao.findById(savedProduct.getId()).get();

        // then
        assertThat(result).isEqualTo(savedProduct);
    }

    @Test
    void 상품을_수정한다() {
        // given
        final Product product = new Product("허브티", "tea.jpg", 1000L);
        final Product savedProduct = productDao.save(product);
        final Product updatedProduct = new Product(savedProduct.getId(), "블랙캣", "cat.jpg", 10000L);

        // when
        productDao.update(updatedProduct);

        // then
        final Product result = productDao.findById(updatedProduct.getId()).get();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("블랙캣"),
                () -> assertThat(result.getImageUrl()).isEqualTo("cat.jpg"),
                () -> assertThat(result.getPrice()).isEqualTo(10000L)
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        final Product product = new Product("허브티", "tea.jpg", 1000L);
        final Product savedProduct = productDao.save(product);

        // when
        productDao.delete(savedProduct.getId());

        // then
        assertThat(productDao.findAll()).isEmpty();
    }
}
