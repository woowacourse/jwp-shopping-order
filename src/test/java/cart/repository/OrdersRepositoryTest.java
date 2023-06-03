package cart.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.fixture.Fixture.TEST_MEMBER;

@JdbcTest
class OrdersRepositoryTest extends RepositoryTest {
    private final OrdersRepository ordersRepository;

    @Autowired
    private OrdersRepositoryTest(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.ordersRepository = new OrdersRepository(ordersDao, ordersCartItemDao, productDao);
    }

    @Test
    void takeOrders() {
        ordersRepository.takeOrders(1L, 2000);
        Assertions.assertThat(ordersDao.findAllByMemberId(1L)).hasSize(3);
    }

    @Test
    void findAllOrdersByMember() {
        Assertions.assertThat(ordersRepository.findAllOrdersByMember(TEST_MEMBER)).hasSize(2);
    }

    @Test
    void findOrdersById() {
        Assertions.assertThat(ordersRepository.findOrdersById(1L).get().getPrice()).isEqualTo(9000);
    }

    @Test
    void findCartItemByOrdersIds() {
        Assertions.assertThat(ordersRepository.findCartItemByOrdersIds(TEST_MEMBER, 2L)).hasSize(2);
    }

    @Test
    void confirmOrdersCreateCoupon() {
        ordersRepository.confirmOrders(1L);
        Assertions.assertThat(ordersDao.findById(1L).get().getConfirmState()).isTrue();
    }

    @Test
    void deleteOrders() {
        ordersRepository.deleteOrders(1L);
        Assertions.assertThat(ordersDao.findById(1L).isEmpty()).isTrue();
    }
}
