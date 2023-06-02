package cart.dao;

import cart.entity.ProductEntity;
import cart.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("Product Dao 테스트")
@Sql("/product_data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductDaoTest {

    @Autowired
    private DataSource dataSource;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(dataSource);
    }

    @Test
    @DisplayName("상품 여러 개 조회 성공")
    void findByIds_success() {
        // given
        final List<Long> ids = List.of(1L, 2L, 3L);

        // when
        final List<ProductEntity> productEntities = productDao.findByIds(ids);

        // then
        assert productEntities != null;
        assertAll(
                () -> assertThat(productEntities).hasSize(ids.size()),
                () -> assertThat(productEntities.get(0).getId()).isEqualTo(ids.get(0)),
                () -> assertThat(productEntities.get(1).getId()).isEqualTo(ids.get(1)),
                () -> assertThat(productEntities.get(2).getId()).isEqualTo(ids.get(2))
        );
    }
    @Test
    @DisplayName("상품 여러 개 조회 실패 - 잘못된 id")
    void findByIds_fail_invalid_id() {
        // given
        final List<Long> ids = List.of(1L, 2L, 3L, 1111L);

        // when, then
        assertThatThrownBy(() -> productDao.findByIds(ids))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
