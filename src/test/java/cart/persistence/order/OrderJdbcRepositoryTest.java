package cart.persistence.order;

import cart.application.repository.MemberRepository;
import cart.domain.Member;
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

import static cart.fixture.MemberFixture.디노_ID포함;
import static cart.fixture.MemberFixture.비버_ID포함;
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
        dinoId = memberRepository.createMember(MemberFixture.디노);
        beaverId = memberRepository.createMember(MemberFixture.비버);
        memberRepository.createMember(MemberFixture.레오);
    }

    @DisplayName("order를 저장한다.")
    @Test
    void createOrderTest() {
        Order order = new Order(11400, 15000, 2000, 디노_ID포함);
        Long orderId = orderJdbcRepository.createOrder(order);
        assertThat(orderId).isPositive();
    }

    @DisplayName("주문 목록 조회")
    @Test
    void findOrdersTest() {
        Member dino = new Member(dinoId, 디노_ID포함.getName(), 디노_ID포함.getEmail(), 디노_ID포함.getEmail());
        Order order1 = new Order(11400, 15000, 2000, dino);
        Long order1Id = orderJdbcRepository.createOrder(order1);

        Order order2 = new Order(12000, 17000, 3000, dino);
        Long order2Id = orderJdbcRepository.createOrder(order2);

        Member beaver = new Member(beaverId, 비버_ID포함.getName(), 비버_ID포함.getEmail(), 비버_ID포함.getPassword());
        Order order3 = new Order(12000, 17000, 3000, beaver);
        Long order3Id = orderJdbcRepository.createOrder(order3);

        Order savedOrder1 = new Order(order1Id, order1.getPaymentPrice(), order1.getTotalPrice(), order1.getPoint(), order1.getMember(), order1.getCreatedAt());
        Order savedOrder2 = new Order(order2Id, order2.getPaymentPrice(), order2.getTotalPrice(), order2.getPoint(), order2.getMember(), order2.getCreatedAt());

        List<Order> dinoOrders = orderJdbcRepository.findOrdersByMemberId(dinoId);

        assertThat(dinoOrders).hasSize(2);
        assertThat(dinoOrders).containsExactlyInAnyOrder(savedOrder1, savedOrder2);
    }

    @DisplayName("특정 주문 조회")
    @Test
    void findOrderTest() {
        Member dino = new Member(dinoId, 디노_ID포함.getName(), 디노_ID포함.getEmail(), 디노_ID포함.getEmail());
        Order order1 = new Order(11400, 15000, 2000, dino);
        Long order1Id = orderJdbcRepository.createOrder(order1);

        Order order2 = new Order(12000, 17000, 3000, dino);
        Long order2Id = orderJdbcRepository.createOrder(order2);

        Order savedOrder1 = new Order(order1Id, order1.getPaymentPrice(), order1.getTotalPrice(), order1.getPoint(), order1.getMember(), order1.getCreatedAt());
        Order savedOrder2 = new Order(order2Id, order2.getPaymentPrice(), order2.getTotalPrice(), order2.getPoint(), order2.getMember(), order2.getCreatedAt());

        Order orderResult = orderJdbcRepository.findOrderBy(order1Id).get();
        assertThat(orderResult).isEqualTo(savedOrder1);
    }

    @DisplayName("존재하지 않는 특정 주문 조회 시 예외발생")
    @Test
    void findNonExistOrderTest() {
        assertThat(orderJdbcRepository.findOrderBy(1L)).isEmpty();
    }
}
