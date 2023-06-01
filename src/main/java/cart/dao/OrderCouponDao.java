package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.member.MemberCoupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("order_coupon");
    }

    private static RowMapper<MemberCoupon> rowMapper() {
        return (rs, rowNum) -> new MemberCoupon(rs.getLong("member_coupon.id"),
                new Coupon(
                        rs.getLong("coupon.id"),
                        rs.getString("coupon.name"),
                        new Discount(rs.getString("coupon.discount_type"), rs.getInt("amount"))
                ));
    }

    public void create(Long orderItemId, Long couponId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("order_item_id", orderItemId)
                .addValue("member_coupon_id", couponId);

        simpleJdbcInsert.execute(params);
    }

    public List<MemberCoupon> findCouponsByOrderItemId(Long orderItemId) {
        String sql = "select * from order_coupon, member_coupon, coupon where order_coupon.member_coupon_id = member_coupon.coupon_id and member_coupon.coupon_id = coupon.id and order_item_id = ?";

        return jdbcTemplate.query(sql, rowMapper(), orderItemId);
    }
}
