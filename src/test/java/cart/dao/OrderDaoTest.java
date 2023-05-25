package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.Order;
import cart.fixture.Fixture;

@JdbcTest
class OrderDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("오더를 추가한다.")
    void addOrder() {
        //given
        final Order order = new Order(10000, Fixture.GOLD_MEMBER, List.of(Fixture.CART_ITEM1));

        //when
        final Long id = orderDao.addOrder(order);

        //then
        assertThat(id).isEqualTo(1L);
    }
}
