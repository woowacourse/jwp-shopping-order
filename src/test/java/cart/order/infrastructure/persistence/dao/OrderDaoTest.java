package cart.order.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.common.DaoTest;
import cart.member.domain.Member;
import cart.member.infrastructure.persistence.dao.MemberDao;
import cart.order.infrastructure.persistence.entity.OrderEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderDao 은(는)")
@DaoTest
class OrderDaoTest {

    @Autowired
    private MemberDao memberDao;

    private Long memberId;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        memberDao.addMember(new Member(null, "email", "123"));
        memberId = memberDao.getMemberByEmail("email").getId();
    }

    @Test
    void 주문을_저장한다() {
        // when
        Long save = 주문_저장(memberId);

        // then
        assertThat(save).isNotNull();
    }

    @Test
    void 주문을_ID_로_조회한다() {
        // given
        Long id = 주문_저장(memberId);

        // when
        OrderEntity orderEntity = orderDao.findById(id).get();

        // then
        OrderEntity expected = new OrderEntity(id, memberId);
        assertThat(orderEntity).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 특정_회원의_주문을_조회한다() {
        // given
        memberDao.addMember(new Member(null, "email1", "1234"));
        Long member2Id = memberDao.getMemberByEmail("email1").getId();
        Long id1 = 주문_저장(memberId);
        Long id2 = 주문_저장(memberId);
        Long id3 = 주문_저장(member2Id);
        Long id4 = 주문_저장(member2Id);

        // when
        List<OrderEntity> actual = orderDao.findAllByMemberId(memberId);

        // then
        List<OrderEntity> expected = List.of(
                new OrderEntity(id1, memberId),
                new OrderEntity(id2, memberId)
        );
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private Long 주문_저장(Long memberId) {
        OrderEntity orderEntity = new OrderEntity(null, memberId);
        return orderDao.save(orderEntity);
    }
}
