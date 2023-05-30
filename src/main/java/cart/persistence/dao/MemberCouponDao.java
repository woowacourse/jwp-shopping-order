package cart.persistence.dao;

import cart.persistence.entity.MemberCouponEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(final MemberCouponEntity memberCoupon) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO member_coupon (member_id, coupon_id, issued_at, expired_at, is_used) "
                    + "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, memberCoupon.getMemberId());
            ps.setLong(2, memberCoupon.getCouponId());
            ps.setTimestamp(3, Timestamp.valueOf(memberCoupon.getIssuedAt()));
            ps.setTimestamp(4, Timestamp.valueOf(memberCoupon.getExpiredAt()));
            ps.setBoolean(5, memberCoupon.isUsed());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean existByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        final String sql = "SELECT COUNT(*) FROM member_coupon WHERE member_id = ? and coupon_id = ?";
        final long count = jdbcTemplate.queryForObject(sql, Long.class, memberId, couponId);
        return count > 0;
    }
}
