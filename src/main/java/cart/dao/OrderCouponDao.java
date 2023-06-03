package cart.dao;

import static java.util.stream.Collectors.toList;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.MemberCoupon;
import cart.entity.OrderCouponEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCouponDao {


    private final static RowMapper<MemberCoupon> memberCouponRowMapper = (rs, rowNum) ->
            new MemberCoupon(
                    rs.getLong("member_coupon.id"),
                    rs.getLong("member_coupon.member_id"),
                    new Coupon(
                            rs.getLong("coupon.id"),
                            rs.getString("coupon.name"),
                            CouponType.from(rs.getString("coupon.type")),
                            rs.getInt("coupon.discount_amount")
                    ),
                    rs.getBoolean("member_coupon.used")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(OrderCouponEntity orderCouponEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(orderCouponEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public void batchSave(List<OrderCouponEntity> orderCouponEntities) {
        final SqlParameterSource[] array = orderCouponEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .collect(toList())
                .toArray(new SqlParameterSource[orderCouponEntities.size()]);
        simpleJdbcInsert.executeBatch(array);
    }

    public List<MemberCoupon> findByOrderId(Long orderId) {
        String sql = "SELECT mc.id, mc.member_id, mc.used, c.id, c.name, c.type, c.discount_amount, oi.order_id " +
                "FROM order_coupon oc " +
                "JOIN member_coupon mc ON oc.member_coupon_id = mc.id " +
                "JOIN coupon c ON mc.coupon_id = c.id " +
                "JOIN order_item oi ON oc.order_item_id = oi.id " +
                "where oi.order_id = ?";
        return jdbcTemplate.query(sql, memberCouponRowMapper, orderId);
    }

    public List<MemberCoupon> findByOrderItemId(Long orderItemId) {
        String sql = "SELECT mc.id, mc.member_id, mc.used, c.id, c.name, c.type, c.discount_amount " +
                "FROM order_coupon oc " +
                "JOIN member_coupon mc ON oc.member_coupon_id = mc.id " +
                "JOIN coupon c ON c.id = mc.coupon_id " +
                "where oc.order_item_id = ?";
        return jdbcTemplate.query(sql, memberCouponRowMapper, orderItemId);
    }

    public List<MemberCoupon> findAllByOrderItemIds(List<Integer> integers) {
        return null;
    }

    public List<OrderCouponEntity> findAllByOrderId(Long orderId) {
        String sql = "SELECT oc.id, oc.order_item_id, oc.member_coupon_id, oi.order_id " +
                "FROM order_coupon oc " +
                "JOIN order_item oi ON oc.order_item_id = oi.id " +
                "WHERE oi.order_id = ?";
        return jdbcTemplate.query(sql, orderCouponRowMapper, orderId);
    }

    private final static RowMapper<OrderCouponEntity> orderCouponRowMapper = (rs, rowNum) ->
            new OrderCouponEntity(
                    rs.getLong("order_coupon.id"),
                    rs.getLong("order_coupon.order_item_id"),
                    rs.getLong("order_coupon.member_coupon_id")
            );

    public List<OrderCouponEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT oc.id, oc.order_item_id, oc.member_coupon_id, oi.order_id " +
                "FROM order_coupon oc " +
                "JOIN order_item oi ON oc.order_item_id = oi.id " +
                "JOIN orders o ON o.id = oi.order_id " +
                "WHERE o.member_id = ?";
        return jdbcTemplate.query(sql, orderCouponRowMapper, memberId);
    }
}
