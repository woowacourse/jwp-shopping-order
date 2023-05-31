package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberEntity;
import cart.entity.OrdersEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrdersDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrdersDao ordersDao;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        ordersDao = new OrdersDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 주문을_저장한다() {
        // given
        MemberEntity savedMember = memberDao.insert(new MemberEntity("우가@a.com", "1234"));
        OrdersEntity ordersEntity = new OrdersEntity(3000L, null, savedMember.getId());

        // when
        ordersDao.insert(ordersEntity);

        // then
        assertThat(ordersDao.findByMemberId(savedMember.getId())).hasSize(1);
    }

    @Test
    void 해당멤버아이디와_주문아이디에_해당하는_주문을_조회한다() {
        // given
        MemberEntity savedMember = memberDao.insert(new MemberEntity("우가@a.com", "1234"));
        OrdersEntity ordersEntity = new OrdersEntity(3000L, 0L, savedMember.getId());

        // when
        OrdersEntity savedOrderEntity = ordersDao.insert(ordersEntity);

        // then
        OrdersEntity result = ordersDao.findByOrderIdAndMemberId(savedOrderEntity.getId(),
                savedMember.getId());

        assertThat(result.getMemberCouponId()).isEqualTo(ordersEntity.getMemberCouponId());
        assertThat(result.getDeliveryFee()).isEqualTo(ordersEntity.getDeliveryFee());
        assertThat(result.getMemberId()).isEqualTo(savedMember.getId());
    }

    @Test
    void 해당멤버의_전체_주문을_조회한다() {
        // given
        MemberEntity savedMember = memberDao.insert(new MemberEntity("우가@a.com", "1234"));
        OrdersEntity ordersEntity1 = new OrdersEntity(3000L, 0L, savedMember.getId());
        OrdersEntity ordersEntity2 = new OrdersEntity(3000L, 0L, savedMember.getId());
        OrdersEntity ordersEntity3 = new OrdersEntity(3000L, 0L, savedMember.getId());
        OrdersEntity ordersEntity4 = new OrdersEntity(3000L, 0L, savedMember.getId());

        // when
        OrdersEntity savedOrderEntity1 = ordersDao.insert(ordersEntity1);
        OrdersEntity savedOrderEntity2 = ordersDao.insert(ordersEntity2);
        OrdersEntity savedOrderEntity3 = ordersDao.insert(ordersEntity3);
        OrdersEntity savedOrderEntity4 = ordersDao.insert(ordersEntity4);

        // then
        List<OrdersEntity> result = ordersDao.findByMemberId(savedMember.getId());
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(List.of(savedOrderEntity1, savedOrderEntity2, savedOrderEntity3, savedOrderEntity4));
    }
}
