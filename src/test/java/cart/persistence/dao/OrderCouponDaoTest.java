package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.persistence.entity.OrderCouponEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import(OrderCouponDao.class)
class OrderCouponDaoTest extends DaoTestHelper {

    @Autowired
    private OrderCouponDao orderCouponDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("상품 쿠폰 정보를 저장한다.")
    void insert() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        final Long 저장된_주문_아이디 = 주문_저장(저장된_져니_아이디);
        final OrderCouponEntity 주문_쿠폰_엔티티 = new OrderCouponEntity(저장된_주문_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // when
        final Long 저장된_주문_쿠폰_아이디 = orderCouponDao.insert(주문_쿠폰_엔티티);

        // then
        final OrderCouponEntity 저장된_주문_쿠폰_엔티티 = 주문_쿠폰_엔티티_조회(저장된_주문_쿠폰_아이디);
        assertThat(저장된_주문_쿠폰_엔티티)
            .extracting(OrderCouponEntity::getOrderId, OrderCouponEntity::getCouponId)
            .contains(저장된_주문_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
    }

    private OrderCouponEntity 주문_쿠폰_엔티티_조회(final Long 저장된_주문_쿠폰_아이디) {
        final String sql = "SELECT order_id, coupon_id FROM order_coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql,
            (rs, count) -> new OrderCouponEntity(
                rs.getLong("order_id"),
                rs.getLong("coupon_id")), 저장된_주문_쿠폰_아이디);
    }
}
