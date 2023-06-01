package cart.dao.order;

import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void clean() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void insert_order_data() {
        // given
        Member member = new Member(1L, "ako@wooteco.com", "Abcd1234@", Rank.DIAMOND, 500_000);
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "포카칩", 1000, "이미지", 10, 0),
                new OrderItem(2L, "스윙칩", 2000, "이미지", 15, 10));
        Order order = new Order(member, orderItems);
        order.calculatePrice();

        // when
        Long result = orderDao.insertOrder(order);

        // then
        assertThat(result).isEqualTo(1L);
    }

}