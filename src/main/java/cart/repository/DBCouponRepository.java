package cart.repository;

import cart.domain.Coupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DBCouponRepository implements CouponRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) ->
            new Coupon(rs.getLong("coupon.id"),
                    rs.getString("coupon.name"),
                    rs.getInt("coupon.discount_value"),
                    rs.getInt("coupon.minimum_order_amount"),
                    rs.getTimestamp("coupon.end_date").toLocalDateTime());

    public DBCouponRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Coupon> findByMemberId(Long memberId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.discount_value, coupon.minimum_order_amount, coupon.end_date " +
                "FROM member_coupon " +
                "INNER JOIN coupon ON member_coupon.coupon_id = coupon.id " +
                "WHERE member_coupon.member_id = ?";

        return jdbcTemplate.query(sql, couponRowMapper, memberId);
    }
}
