package cart.dao;

import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.OrderFixture.ORDER_1;
import static cart.fixture.OrderItemsFixture.ORDER_ITEMS_1;
import static cart.fixture.PaymentFixture.PAYMENT_1;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import cart.domain.Member;
import cart.domain.Order;
import cart.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;
    private MemberDao memberDao;
    private Member member;
    private Order order;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);

        Long memberId = memberDao.addMember(MEMBER_1);
        member = new Member(memberId, MEMBER_1.getEmail(), MEMBER_1.getPassword());
        order = new Order(1L, LocalDateTime.parse("2022-06-03T23:00:15.317115"), PAYMENT_1, ORDER_ITEMS_1, member);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void save() {
        // when
        Long orderId = orderDao.save(ORDER_1);

        // then
        assertThat(orderId).isNotNull();
    }

    @Test
    @DisplayName("ID로 주문 내역을 조회한다.")
    void findById() {
        // given
        Long id = orderDao.save(order);

        // when
        OrderEntity orderEntity = orderDao.findById(id);

        // then
        assertThat(orderEntity)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(new OrderEntity(
                        id,
                        member.getId(),
                        order.getCreatedAt()));
    }

    @Test
    @DisplayName("사용자의 전체 주문 내역을 조회한다.")
    void findAllByMemberId() {
        // given
        Long id = orderDao.save(order);

        // when
        List<OrderEntity> orderEntities = orderDao.findAllByMemberId(member.getId());

        // then
        assertThat(orderEntities)
                .hasSize(1)
                .extracting(OrderEntity::getId)
                .containsExactly(id);
    }
}
