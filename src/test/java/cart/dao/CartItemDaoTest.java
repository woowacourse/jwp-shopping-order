package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.CartItemProductDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_cart_item.sql"})
class CartItemDaoTest {

    private CartItemDao cartItemDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @ParameterizedTest
    @CsvSource(value = {"4:true", "3:false"}, delimiter = ':')
    @DisplayName("존재하는 장바구니 id 라면 false, 존재하지 않는다면 true 가 반환된다.")
    void isNonExistingId(long id, boolean expectedResult) {
        // when
        boolean result = cartItemDao.isNonExistingId(id);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("장바구니에 있는 상품들 중, 요청한 id를 가진 모든 장바구니 내역을 조회할 수 있다.")
    void findAllByIds() {
        // given
        List<Long> ids = List.of(1L, 2L);

        // when
        List<CartItemProductDto> items = cartItemDao.findAllByIds(ids);

        // then
        assertThat(items)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .comparingOnlyFields("cartItemId", "productId", "memberId")
            .isEqualTo(List.of(
                new CartItemProductDto(1L, 1L, 1L, "", 0, "", 0, ""),
                new CartItemProductDto(2L, 2L, 1L, "", 0, "", 0, "")
            ));
    }
}