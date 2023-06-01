package cart.dao;

import cart.domain.Coupon;
import cart.domain.MemberCoupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class MemberCouponRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberCouponRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(final Long memberId, final MemberCoupon memberCoupon) {
        String sql = "INSERT INTO member_coupon (member_id, coupon_id, issued_at, expired_at, is_used) VALUES (?, ?, ?, ?, ?) ";
        jdbcTemplate.update(sql,
                ps -> {
                    ps.setLong(1, memberId);
                    ps.setLong(2, memberCoupon.getCoupon().getId());
                    ps.setTimestamp(3, Timestamp.valueOf(memberCoupon.getIssuedDate()));
                    ps.setTimestamp(4, Timestamp.valueOf(memberCoupon.getExpiredDate()));
                    ps.setBoolean(5, memberCoupon.isUsed());
                });
    }

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
                    new Coupon(rs.getLong("id"), rs.getString("name"), rs.getInt("discount_rate"), rs.getInt("period"), rs.getTimestamp("expired_at").toLocalDateTime()),
                    rs.getBoolean("is_used"),
                    rs.getTimestamp("issued_at").toLocalDateTime(),
                    rs.getTimestamp("expired_at").toLocalDateTime()
            );
        }
    }
}
