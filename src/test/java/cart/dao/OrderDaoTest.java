package cart.dao;

import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.exception.notfound.OrderNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import(OrderDao.class)
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문을 저장하고 조회한다.")
    @Test
    void insertAndFind() {
        // given
        final Member member = new Member(1L, "a@a.com", null);
        final Order order = new Order(member, new MemberPoint(1000), new MemberPoint(500), new DeliveryFee(3000));
        final Long orderId = orderDao.insert(order);

        // when
        final Order findOrder = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

        // then
        assertThat(findOrder.getId()).isEqualTo(orderId);
    }

    @DisplayName("사용자가 가지고 있는 모든 주문을 조회한다.")
    @Test
    void findAllByMemberId() {
        // given
        final Member member = new Member(1L, "a@a.com", null);
        final Order order1 = new Order(member, new MemberPoint(1000), new MemberPoint(500), new DeliveryFee(3000));
        final Order order2 = new Order(member, new MemberPoint(2000), new MemberPoint(500), new DeliveryFee(3000));
        final Order order3 = new Order(member, new MemberPoint(3000), new MemberPoint(500), new DeliveryFee(0));
        final Long orderId1 = orderDao.insert(order1);
        final Long orderId2 = orderDao.insert(order2);
        final Long orderId3 = orderDao.insert(order3);

        // when
        final List<Order> orders = orderDao.findAllByMemberId(member.getId());

        // then
        assertAll(
                () -> assertThat(orders).hasSize(3),
                () -> assertThat(orders.get(0).getUsedPoint()).isEqualTo(new MemberPoint(1000)),
                () -> assertThat(orders.get(0).getDeliveryFee()).isEqualTo(new DeliveryFee(3000)),
                () -> assertThat(orders.get(1).getUsedPoint()).isEqualTo(new MemberPoint(2000)),
                () -> assertThat(orders.get(1).getDeliveryFee()).isEqualTo(new DeliveryFee(3000)),
                () -> assertThat(orders.get(2).getUsedPoint()).isEqualTo(new MemberPoint(3000)),
                () -> assertThat(orders.get(2).getDeliveryFee()).isEqualTo(new DeliveryFee(0))
        );
    }
}
