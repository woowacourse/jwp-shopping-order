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
    private OrdersDaoTest(JdbcTemplate jdbcTemplate){
        this.ordersDao = new OrdersDao(jdbcTemplate);
    }
    @Test
    @DisplayName("주문을 받는다")
    void createOrder(){
        Assertions.assertThat(ordersDao.createOrders(1L,2000,1000)).isEqualTo(3L);
    }

    @Test
    @DisplayName("사용자의 모든 주문 내역을 찾는다")
    void findAllByMemberId(){
        Assertions.assertThat(ordersDao.findAllByMemberId(1l)).hasSize(2);
    }

    @Test
    @DisplayName("해당 id의 주문 내역을 가져온다")
    void findByIdTest(){
        Assertions.assertThat(ordersDao.findById(1L).getOriginalPrice()).isEqualTo(10000);
    }
}
