package cart.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@JdbcTest
class OrdersDaoTest {
    private final OrdersDao ordersDao;

    @Autowired
    private OrdersDaoTest(JdbcTemplate jdbcTemplate) {
        this.ordersDao = new OrdersDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문을 받는다")
    void createOrder() {
        Assertions.assertThatNoException().isThrownBy(()->ordersDao.createOrders(1L,1000));
    }

    @Test
    @DisplayName("사용자의 모든 주문 내역을 찾는다")
    void findAllByMemberId() {
        Assertions.assertThat(ordersDao.findAllByMemberId(1l)).hasSize(2);
    }

    @Test
    @DisplayName("해당 id의 주문 내역을 가져온다")
    void findByIdTest() {
        Assertions.assertThat(ordersDao.findById(1L).get().getPrice()).isEqualTo(9000);
    }

    @Test
    @DisplayName("제품이 없을 시 optional로 가져온다")
    void findNonTest() {
        Assertions.assertThat(ordersDao.findById(4L).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("해당 아이디의 주문을 승락처리한다")
    void updateConfirmById() {
        ordersDao.updateConfirmById(1L);
        Assertions.assertThat(ordersDao.findById(1L).get().getConfirmState()).isTrue();
    }

    @Test
    @DisplayName("해당 주문 내역을 지운다")
    void deleteByIdTest() {
        ordersDao.deleteById(1L);
        Assertions.assertThat(ordersDao.findById(1L).isEmpty()).isTrue();
    }
}
