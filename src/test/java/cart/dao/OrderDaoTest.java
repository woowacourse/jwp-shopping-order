package cart.dao;

import cart.domain.Member.Member;
import cart.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class OrderDaoTest {
    public OrderDao orderDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @Test
    void 주문_엔티티를_받아_저장한다() {
        // given
//        final OrderEntity orderEntity = new OrderEntity(null, 1L, null);
//        final Member member = new Member(1L, "a@a.com", "1234");
//
//        // when
//        orderDao.save(member, orderEntity);
//
//        // then
//        final List<OrderEntity> order = orderDao.findOrderByMemberId(member.getId());
//        assertAll(
//                () -> assertThat(order).hasSize(1),
//                () -> assertThat(order.get(0).getId()).isPositive(),
//                () -> assertThat(order.get(0).getMemberId()).isEqualTo(1L),
//                () -> assertThat(order.get(0).getOrderDate()).isBefore(new Timestamp(System.currentTimeMillis()))
//        );
    }

    @Test
    void 주문에_대한_상품들을_저장한다() {
        // given
//        final OrderEntity orderEntity = new OrderEntity(null, 1L, null);
//
//        final Member member = new Member(1L, "a@a.com", "1234");
//
//        Long orderId = orderDao.save(member, orderEntity);
//
//        final OrderItemEntity orderItem1 = new OrderItemEntity(orderId, 1L, 1, 1000);
//        final OrderItemEntity orderItem2 = new OrderItemEntity(orderId, 2L, 2, 2000);
//
//        // when
//        orderItemDao.save(List.of(orderItem1, orderItem2));
//
//
//        // then
//        List<OrderItemEntity> orderItems = orderItemDao.findByOrderId(orderId);
//
//        assertAll(
//                () -> assertThat(orderItems).hasSize(2),
//                () -> assertThat(orderItems.get(0).getId()).isPositive(),
//                () -> assertThat(orderItems.get(0).getOrderId()).isEqualTo(1L),
//                () -> assertThat(orderItems.get(0).getQuantity()).isPositive()
//        );
    }

    @Test
    void 주문에_대한_상품들을_가져온다() {

        final Member member = new Member(1L, "a@a.com", "1234");
        Map<Long, List<OrderItemEntity>> orderItems = orderItemDao.findOrderItemsByMemberId(member.getId());
        orderItems.values().forEach(list -> list.forEach(ele -> System.out.println("주문번호 : " + ele.getId())));

    }
}