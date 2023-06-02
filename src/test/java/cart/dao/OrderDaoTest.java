package cart.dao;

import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.MemberFixture.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.entity.OrderEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderDao 은(는)")
@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 주문을_저장한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        OrderEntity orderEntity = new OrderEntity(MEMBER.getId());

        // when
        Long actual = orderDao.save(orderEntity);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 아이디를_통해_주문을_찾는다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        OrderEntity orderEntity = new OrderEntity(MEMBER.getId());
        Long orderId = orderDao.save(orderEntity);

        // when
        OrderEntity actual = orderDao.findById(orderId).get();

        // then
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getMemberId()).isEqualTo(MEMBER.getId())
        );
    }

    @Test
    void 해당하는_아이디의_주문이_없다면_빈값() {
        // when
        Optional<OrderEntity> actual = orderDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 멤버_아이디를_통해_모든_주문을_찾는다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        orderDao.save(new OrderEntity(MEMBER.getId()));
        orderDao.save(new OrderEntity(MEMBER.getId()));
        orderDao.save(new OrderEntity(MEMBER.getId()));

        insertMember(new Member(MEMBER.getId() + 1, "email@naver.com", "password"), jdbcTemplate);
        orderDao.save(new OrderEntity(MEMBER.getId()+1));
        orderDao.save(new OrderEntity(MEMBER.getId()+1));

        // when
        List<OrderEntity> actual = orderDao.findAllByMemberId(MEMBER.getId());

        // then
        assertThat(actual.size()).isEqualTo(3);
    }
}
