package cart.persistence.coupon;

import cart.application.repository.CouponRepository;
import cart.domain.Coupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) ->
            new Coupon(
                    rs.getLong("id"),
                    rs.getString("`name`"),
                    rs.getInt("min_amount"),
                    rs.getInt("discount_percent")
            );

    public CouponJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Coupon> findByMemberId(final Long memberId) {
        final String sql = "SELECT coupon.* " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.member_id = ?";
        return jdbcTemplate.query(sql, couponRowMapper, memberId);
    }
}
