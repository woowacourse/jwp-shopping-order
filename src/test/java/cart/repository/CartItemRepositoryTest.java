package cart.repository;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@JdbcTest
class CartItemRepositoryTest extends RepositoryTest {
    private final CartItemRepository cartItemRepository;

    @Autowired
    private CartItemRepositoryTest(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.cartItemRepository = new CartItemRepository(cartItemDao, ordersCartItemDao, productDao);
    }

    @Test
    void changeCartItemToOrdersItem() {
        cartItemRepository.changeCartItemToOrdersItem(1L, List.of(1L, 2L));
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(cartItemDao.findById(1L)).isNull();
            softly.assertThat(cartItemDao.findById(2L)).isNull();
        });
    }

    @Test
    void findTotalPriceByCartId() {
        Assertions.assertThat(cartItemRepository.findTotalPriceByCartId(1L)).isEqualTo(2000);
    }
}
