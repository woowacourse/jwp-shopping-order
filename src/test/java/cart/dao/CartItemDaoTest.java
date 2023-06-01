package cart.dao;

import cart.entity.CartItemWithProductEntity;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("CartItem Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemDaoTest {

    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.cartItemDao = new CartItemDao(dataSource);
    }

    @Nested
    @DisplayName("id 들로 product join 된 cart item 조회")
    class FindProductDetailByIds {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final List<Long> ids = List.of(1L, 2L);

            // when
            final List<CartItemWithProductEntity> entities = cartItemDao.findProductDetailByIds(ids);

            // then
            assert entities != null;
            assertAll(
                    () -> assertThat(entities).hasSize(ids.size()),
                    () -> assertThat(entities.stream()
                            .map(CartItemWithProductEntity::getProductId)
                            .collect(Collectors.toUnmodifiableList())).containsAll(ids)
            );
        }

        @Test
        @DisplayName("실패 - input 과 output 결과 사이즈가 다름")
        void fail_when_input_and_output_size_different() {
            // given
            final List<Long> validIds = List.of(1L, 2L);
            final List<Long> invalidIds = List.of(100L, 1000L);
            final List<Long> sourceIds = Stream.of(validIds, invalidIds)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toUnmodifiableList());

            // when, then
            assertThatThrownBy(() -> cartItemDao.findProductDetailByIds(sourceIds))
                    .isInstanceOf(CartItemNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("다중 삭제")
    class DeleteAllByIds {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final List<Long> ids = List.of(1L, 2L);

            // when
            cartItemDao.deleteAllByIds(ids);

            // then
            final String sql = "SELECT id FROM cart_item WHERE id IN (:ids)";
            final MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("ids", ids);
            assertThat(namedParameterJdbcTemplate.query(sql, params, (rs, rn) -> rs.getLong("id")))
                    .isEmpty();
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 cart item id")
        void fail_when_cartItem_not_found() {
            // given
            final List<Long> invalidIds = List.of(100L, 200L);

            // when, then
            assertThatThrownBy(() -> cartItemDao.deleteAllByIds(invalidIds))
                    .isInstanceOf(CartItemNotFoundException.class);

        }
    }
}
