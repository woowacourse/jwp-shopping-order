package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.persistence.entity.OrderEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class OrderDaoTest extends DaoTestHelper {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("주문 정보를 저장한다.")
    void insert() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, LocalDateTime.now());

        // when
        final Long 저장된_주문_아이디 = orderDao.insert(주문_엔티티);

        // then
        final OrderEntity 저장된_주문_엔티티 = 주문_엔티티_조회(저장된_주문_아이디);
        assertThat(저장된_주문_엔티티)
            .extracting(OrderEntity::getId, OrderEntity::getMemberId, OrderEntity::getTotalPrice,
                OrderEntity::getDiscountedTotalPrice, OrderEntity::getDeliveryPrice)
            .containsExactly(저장된_주문_아이디, 저장된_져니_아이디, 10000, 9000, 3000);
    }

    @Test
    @DisplayName("특정 회원이 주문한 전체 횟수를 구한다.")
    void countByMemberId() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, LocalDateTime.now());
        orderDao.insert(주문_엔티티);

        // when
        final Long 저장된_주문_횟수 = orderDao.countByMemberId(저장된_져니_아이디);

        // then
        assertThat(저장된_주문_횟수)
            .isEqualTo(1L);
    }

    private OrderEntity 주문_엔티티_조회(final Long 저장된_주문_아이디) {
        final String sql = "SELECT id, member_id, total_price, discounted_total_price, delivery_price, ordered_at "
            + "FROM `order` WHERE id = ?";
        final OrderEntity 저장된_주문_엔티티 = jdbcTemplate.queryForObject(sql, (rs, count) ->
            new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getInt("total_price"),
                rs.getInt("discounted_total_price"),
                rs.getInt("delivery_price"),
                rs.getTimestamp("ordered_at").toLocalDateTime()), 저장된_주문_아이디);
        return 저장된_주문_엔티티;
    }
}
