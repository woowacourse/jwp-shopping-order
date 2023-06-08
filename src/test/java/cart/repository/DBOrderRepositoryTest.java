package cart.repository;

import cart.domain.Member;
import cart.domain.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixtures.MemberFixtures.MEMBER1;
import static cart.fixtures.OrderFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBOrderRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new DBOrderRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("주문 정보를 저장한다.")
    void saveTest() {
        // given
        Order newOrderToSave = NEW_ORDER_TO_INSERT;
        Order newOrder = NEW_ORDER;
        Member orderMember = newOrderToSave.getMember();

        // when
        orderRepository.save(newOrderToSave);

        // then
        assertThat(orderRepository.findByMemberId(orderMember.getId())).contains(newOrder);
    }

    @Test
    @DisplayName("ID에 해당하는 Member의 주문 목록을 가져온다.")
    void findByMemberIdTest() {
        // given
        Member member = MEMBER1;
        List<Order> memberOrders = List.of(ORDER1, ORDER2);

        // when
        List<Order> findOrders = orderRepository.findByMemberId(member.getId());

        // then
        assertThat(findOrders).isEqualTo(memberOrders);
    }

    @Test
    @DisplayName("ID에 해당하는 주문을 가져온다.")
    void findByIdTest() {
        // given
        Order order = ORDER1;
        Long orderId = order.getId();

        // when
        Order findOrder = orderRepository.findById(orderId);

        // then
        assertThat(findOrder).isEqualTo(order);
    }
}