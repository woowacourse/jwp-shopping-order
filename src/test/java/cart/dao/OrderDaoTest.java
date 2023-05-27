package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.entity.Order;
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
        final Long savedOrderId = createOrder(10000, Fixture.GOLD_MEMBER, List.of(Fixture.CART_ITEM1));

        //then
        assertThat(savedOrderId).isEqualTo(1L);
    }

    private Long createOrder(int price, Member member, List<CartItem> cartItems) {
        final Order order = new Order(price, member.getId());
        return orderDao.addOrder(order);
    }

//    @Test
//    @DisplayName("id를 통해 order를 조회한다.")
//    void findOrderById() {
//        //given
//        final Long savedOrderId = createOrder(10000, Fixture.GOLD_MEMBER, List.of(Fixture.CART_ITEM1));
//
//        //when
//        final Order result = orderDao.findById(savedOrderId);
//
//        //then
//        Assertions.assertAll(
//                () -> assertThat(result.getMember()).isEqualTo(Fixture.GOLD_MEMBER),
//                () -> assertThat(result.getId()).isEqualTo(savedOrderId),
//                () -> assertThat(result.getPrice()).isEqualTo(10000)
//        );
//    }
}
