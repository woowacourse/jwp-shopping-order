package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Order;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);

        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문_생성_테스트() {
        final Member memberFixture = getSavedMemberFixture("testEmail");
        final Order order = new Order(memberFixture, 50_000, 45_000);

        orderDao.saveOrder(order);

        final List<Order> allOrders = orderDao.findAll();
        assertThat(allOrders).hasSize(1);
        assertThat(allOrders.get(0).getMember().getEmail()).isEqualTo("testEmail");
    }

    @Test
    void 사용자_주문_목록_조회_테스트() {
        //given
        final Member memberFixtureA = getSavedMemberFixture("testEmailA");
        final Order orderA = new Order(memberFixtureA, 70_000, 65_000);
        final Member memberFixtureB = getSavedMemberFixture("testEmailB");
        final Order orderB = new Order(memberFixtureB, 30_000, 28_000);
        orderDao.saveOrder(orderA);
        orderDao.saveOrder(orderB);

        //when
        final List<Order> orderByMemberA = orderDao.findOrderByMember(memberFixtureA.getId());

        //then
        assertThat(orderByMemberA).hasSize(1);
        final Order memberAOrder = orderByMemberA.get(0);
        assertThat(memberAOrder.getMember().getEmail()).isEqualTo("testEmailA");
        assertThat(memberAOrder.getTotalPrice()).isEqualTo(70_000);
    }

    @Test
    void 식별자로_주문_조회_테스트() {
        //given
        final Member memberFixtureA = getSavedMemberFixture("testEmailA");
        final Order orderA = new Order(memberFixtureA, 70_000, 65_000);
        final Long savedOrderId = orderDao.saveOrder(orderA);

        final Member memberFixtureB = getSavedMemberFixture("testEmailB");
        final Order orderB = new Order(memberFixtureB, 30_000, 28_000);
        orderDao.saveOrder(orderB);

        //when
        final Optional<Order> byId = orderDao.findById(savedOrderId);

        //then
        assertThat(byId).isNotEmpty();
        assertThat(byId.get().getId()).isEqualTo(savedOrderId);
        assertThat(byId.get().getTotalPrice()).isEqualTo(70_000);
    }

    private Member getSavedMemberFixture(final String email) {
        final Member member = new Member(email, "password");
        final Long saveMemberId = memberDao.saveMember(member);

        return new Member(saveMemberId, member.getEmail(), member.getPassword());
    }
}
