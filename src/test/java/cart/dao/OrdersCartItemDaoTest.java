package cart.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class OrdersCartItemDaoTest {
    private final OrdersCartItemDao ordersCartItemDao;
    @Autowired
    private OrdersCartItemDaoTest(JdbcTemplate jdbcTemplate){
        this.ordersCartItemDao = new OrdersCartItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문내역에 상품을 등록한다.")
    void createOrdersIdCartItemId() {
        ordersCartItemDao.createOrdersIdCartItemId(1L,2L,3);
        Assertions.assertThat(ordersCartItemDao.findAllByOrdersId(1L)).hasSize(2);
    }

    @Test
    void findAllByOrdersId() {
        Assertions.assertThat(ordersCartItemDao.findAllByOrdersId(1L)).hasSize(1);
    }
}
