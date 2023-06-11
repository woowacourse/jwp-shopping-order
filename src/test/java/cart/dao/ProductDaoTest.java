package cart.dao;

import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @DisplayName("특정 아이디들을 모두 조회할 수 있다 - 모든 id가 존재하는 경우")
    @Test
    void existByIds() {
        // given
        List<Long> ids = List.of(1L, 2L, 3L);

        // when
        List<Product> findProducts = productDao.getAllByIds(ids);

        // then
        assertThat(findProducts).usingRecursiveFieldByFieldElementComparator()
                                .contains(상품1_치킨, 상품2_샐러드, 상품3_피자);
    }

    @DisplayName("특정 아이디들이 존재하는지 확인할 수 있다 - 존재하지 않는 id가 포함된 경우")
    @Test
    void notExistByIds() {
        // given
        List<Long> ids = List.of(1L, 2L, 100L);

        // when
        List<Product> findProducts = productDao.getAllByIds(ids);

        // then
        assertThat(findProducts).usingRecursiveFieldByFieldElementComparator()
                                .contains(상품1_치킨, 상품2_샐러드);
    }
}
