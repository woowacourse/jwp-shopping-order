package cart.persistence.dao;

import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.MemberCouponEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final RowMapper<MemberCouponDto> memberCouponDtoRowMapper = (rs, count) ->
        new MemberCouponDto(
            rs.getLong("memberId"),
            rs.getString("memberName"),
            rs.getString("password"),
            rs.getLong("couponId"),
            rs.getString("couponName"),
            rs.getInt("period"),
            rs.getInt("discount_rate"),
            rs.getTimestamp("expired_at").toLocalDateTime(),
            rs.getTimestamp("issued_at").toLocalDateTime(),
            rs.getBoolean("is_used")
        );

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

    public Optional<MemberCouponDto> findByMemberIdAndCouponId(final Long memberId, final Long couponId) {
        final String sql = "SELECT m.id AS memberId, m.name AS memberName, m.password, "
            + " c.id AS couponId, c.name AS couponName, c.discount_rate, c.period, mc.expired_at, "
            + " mc.issued_at, mc.is_used"
            + " FROM member m"
            + " INNER JOIN member_coupon mc ON mc.member_id = m.id"
            + " INNER JOIN coupon c on mc.coupon_id = c.id"
            + " WHERE mc.member_id = ? and mc.coupon_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, memberCouponDtoRowMapper, memberId, couponId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<MemberCouponDto> findMyCouponsByName(final String memberName) {
        final String query = "SELECT m.id AS memberId, m.name AS memberName, m.password, "
            + " c.id AS couponId, c.name AS couponName, c.discount_rate, c.period, mc.expired_at, "
            + " mc.issued_at, mc.is_used"
            + " FROM member m"
            + " INNER JOIN member_coupon mc ON mc.member_id = m.id"
            + " INNER JOIN coupon c on mc.coupon_id = c.id"
            + " WHERE m.name = ?";
        return jdbcTemplate.query(query, memberCouponDtoRowMapper, memberName);
    }

    public int updateUsed(final Long memberId, final Long couponId) {
        final String sql = "UPDATE member_coupon SET is_used = 1 "
            + "WHERE member_id = ? and coupon_id = ?";
        return jdbcTemplate.update(sql, memberId, couponId);
    }
}
