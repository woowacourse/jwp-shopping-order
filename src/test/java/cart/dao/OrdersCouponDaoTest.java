package cart.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrdersCouponDaoTest {
    private OrdersCouponDao ordersCouponDao;

    @Autowired
    public OrdersCouponDaoTest(JdbcTemplate jdbcTemplate) {
        this.ordersCouponDao = new OrdersCouponDao(jdbcTemplate);
    }

    @Test
    void createOrderCoupon() {
        Assertions.assertThatNoException().isThrownBy(() ->
                ordersCouponDao.createOrderCoupon(1l, 1l)
        );
    }

    @Test
    void finAllByOrdersId() {
        Assertions.assertThat(ordersCouponDao.finAllByOrdersId(1l))
                .hasSize(1);
    }
}
