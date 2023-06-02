package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_cart_item.sql"})
class ProductDaoTest {

    private ProductDao productDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @ParameterizedTest
    @CsvSource(value = {"3:false", "4:true"}, delimiter = ':')
    @DisplayName("존재하는 상품 id 라면 false, 존재하지 않는다면 true 가 반환된다.")
    void isNonExistingId(long productId, boolean expected) {
        // when
        boolean result = productDao.isNonExistingId(productId);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
