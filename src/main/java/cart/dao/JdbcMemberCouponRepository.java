package cart.dao;

import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcMemberCouponRepository implements MemberCouponRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberCouponRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(final MemberCoupon memberCoupon) {

    }

    @Override
    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        String sql = "SELECT * FROM member_coupon JOIN coupon ON member_coupon.coupon_id = coupon.id WHERE member_id = ?";
        return jdbcTemplate.query(sql,
                ps -> ps.setLong(1, memberId),
                new MemberCouponRowMapper());
    }

    private static class MemberCouponRowMapper implements RowMapper<MemberCoupon> {
        @Override
        public MemberCoupon mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberCoupon(
                    new Coupon(rs.getString("name"), rs.getInt("discount_rate"), rs.getInt("period"), rs.getTimestamp("expired_date").toLocalDateTime()),
                    rs.getBoolean("is_used"),
                    rs.getTimestamp("issued_date").toLocalDateTime(),
                    rs.getTimestamp("expired_date").toLocalDateTime()
            );
        }
    }
}
