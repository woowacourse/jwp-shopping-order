package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.entity.OrderProductEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import({OrderProductDao.class})
class OrderProductDaoTest extends DaoTestHelper {

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("주어진 주문 상품들을 저장한다.")
    void saveAll() {
        // given
        final Long 저장된_치킨_아이디 = 치킨_저장();
        final Long 저장된_피자_아이디 = 피자_저장();
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        final Long 저장된_주문_아이디 = 주문_저장(저장된_져니_아이디);

        final List<OrderProductEntity> 주문_상품_엔티티들 = List.of(
            new OrderProductEntity(저장된_주문_아이디, 저장된_치킨_아이디, 20000),
            new OrderProductEntity(저장된_주문_아이디, 저장된_피자_아이디, 30000));

        // when
        orderProductDao.saveAll(주문_상품_엔티티들);

        // then
        final List<OrderProductEntity> 저장된_주문_상품_엔티티들 = 주문_상품_엔티티_조회(저장된_주문_아이디);
        assertThat(저장된_주문_상품_엔티티들)
            .extracting(OrderProductEntity::getOrderId, OrderProductEntity::getProductId,
                OrderProductEntity::getOrderProductPrice)
            .containsExactly(
                tuple(저장된_주문_아이디, 저장된_치킨_아이디, 20000),
                tuple(저장된_주문_아이디, 저장된_피자_아이디, 30000));
    }

    private List<OrderProductEntity> 주문_상품_엔티티_조회(final Long 저장된_주문_아이디) {
        final String sql = "SELECT order_id, product_id, ordered_product_price FROM order_product WHERE order_id = ?";
        return jdbcTemplate.query(sql, (rs, count) -> new OrderProductEntity(
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("ordered_product_price")), 저장된_주문_아이디);
    }
}
