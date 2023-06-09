package cart.persistence.order;

import cart.application.repository.member.MemberRepository;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.point.Point;
import cart.fixture.ProductFixture;
import cart.persistence.coupon.CouponJdbcRepository;
import cart.persistence.member.MemberJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

import static cart.fixture.MemberFixture.레오;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class OrderJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberRepository memberRepository;
    private OrderJdbcRepository orderJdbcRepository;

    @BeforeEach
    void setUp() {
        this.orderJdbcRepository = new OrderJdbcRepository(
                jdbcTemplate, new CouponJdbcRepository(jdbcTemplate), new OrderedItemJdbcRepository(jdbcTemplate));
        this.memberRepository = new MemberJdbcRepository(jdbcTemplate);

    }

    @DisplayName("order를 저장한다.")
    @Test
    void createOrderTest() {
        OrderItems orderItems = new OrderItems(List.of(
                OrderItem.of(2, ProductFixture.통구이_ID포함),
                OrderItem.of(3, ProductFixture.배변패드_ID포함),
                OrderItem.of(1, ProductFixture.꼬리요리_ID포함)
        ));
        Long memberId = memberRepository.createMember(레오);
        Coupons coupons = new Coupons(Collections.emptyList());
        Order order = new Order(new Member(memberId, 레오.getName(), 레오.getEmail(), 레오.getPassword()), orderItems, coupons, 31500, new Point(0));
        Long orderId = orderJdbcRepository.createOrder(order);
        assertThat(orderJdbcRepository.findByOrderId(memberId, orderId).get()).usingRecursiveComparison().ignoringFields("id", "orderItems").isEqualTo(order);
    }
}
