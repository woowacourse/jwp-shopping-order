package cart.persistence.order;

import cart.application.repository.MemberRepository;
import cart.domain.order.Order;
import cart.fixture.MemberFixture;
import cart.persistence.member.MemberJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class OrderJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderJdbcRepository orderJdbcRepository;
    private MemberRepository memberRepository;

    private long dinoId;
    private long beaverId;
    private long leoId;

    @BeforeEach
    void setUp() {
        this.orderJdbcRepository = new OrderJdbcRepository(jdbcTemplate);
        this.memberRepository = new MemberJdbcRepository(jdbcTemplate);
        memberRepository.createMember(MemberFixture.디노);
        memberRepository.createMember(MemberFixture.비버);
        memberRepository.createMember(MemberFixture.레오);
    }

    @DisplayName("order를 저장한다.")
    @Test
    void createOrderTest() {
        Order order = new Order(11400, 15000, 2000, MemberFixture.디노_ID포함);
        Long orderId = orderJdbcRepository.createOrder(order);
        assertThat(orderId).isPositive();
    }

    @DisplayName("order조회")
    @Test
    void findOrdersTest() {
        Order order1 = new Order(11400, 15000, 2000, MemberFixture.디노_ID포함);
        Long order1Id = orderJdbcRepository.createOrder(order1);

        Order order2 = new Order(12000, 17000, 3000, MemberFixture.디노_ID포함);
        Long order2Id = orderJdbcRepository.createOrder(order2);

        Order order3 = new Order(12000, 17000, 3000, MemberFixture.레오_ID포함);
        Long order3Id = orderJdbcRepository.createOrder(order3);

        Order savedOrder1 = new Order(order1Id, order1.getPaymentPrice(), order1.getTotalPrice(), order1.getPoint(), order1.getMember());
        Order savedOrder2 = new Order(order2Id, order2.getPaymentPrice(), order2.getTotalPrice(), order2.getPoint(), order2.getMember());


        List<Order> dinoOrders = orderJdbcRepository.findOrdersByMemberId(MemberFixture.디노_ID포함.getId());

        assertThat(dinoOrders).hasSize(2);
        assertThat(dinoOrders).containsExactlyInAnyOrder(savedOrder1, savedOrder2);
    }
}
