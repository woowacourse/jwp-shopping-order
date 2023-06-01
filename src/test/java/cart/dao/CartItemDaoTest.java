package cart.dao;

import cart.entity.CartItemWithProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@DisplayName("CartItem Dao 테스트")
@Sql("/data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemDaoTest {

    @Autowired
    private DataSource dataSource;
    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        this.cartItemDao = new CartItemDao(dataSource);
    }

    @Test
    @DisplayName("id 들로 product join 된 cart item 조회 성공")
    void findProductDetailByIds_success() {
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
}
