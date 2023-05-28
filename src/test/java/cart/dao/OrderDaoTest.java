package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Order;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@Import({OrderDao.class, MemberDao.class})
@JdbcTest
class OrderDaoTest {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 주문_정보_저장_테스트() {
        //given
        final Member savedMember = memberDao.addMember(
                new Member(null, "email", "password")
        );
        final Order order = new Order(savedMember, 50_000, 45_000);

        //when
        orderDao.saveOrder(order);

        //then
        final List<Order> allOrders = orderDao.findAll();
        assertThat(allOrders).hasSize(1);
        final Order savedOrder = allOrders.get(0);
        assertThat(savedOrder.getFinalPrice()).isEqualTo(45_000);
    }
}
