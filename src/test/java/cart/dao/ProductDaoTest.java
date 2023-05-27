package cart.dao;

import static cart.fixtures.ProductFixtures.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Nested
    @DisplayName("상품 ID로 상품 존재 여부 확인 시")
    class isNotExistById {

        @Test
        @DisplayName("상품이 존재하지 않으면 TRUE를 반환한다.")
        void isNotExist_true() {
            // given
            Long notExistId = -1L;

            // when, then
            assertThat(productDao.isNotExistById(notExistId)).isTrue();
        }

        @Test
        @DisplayName("상품 존재하면 FALSE를 반환한다.")
        void isExist_false() {
            // when, then
            assertThat(productDao.isNotExistById(CHICKEN.ID)).isFalse();
        }
    }
}
