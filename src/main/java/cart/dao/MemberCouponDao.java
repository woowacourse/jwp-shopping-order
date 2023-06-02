package cart.dao;

import cart.domain.Coupon;
import cart.entity.MemberCouponEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) ->
            MemberCouponEntity.of(
                    rs.getLong("id"),
                    rs.getBoolean("is_used"),
                    rs.getLong("member_id"),
                    rs.getLong("coupon_id")
            );

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberCouponEntity> findByCouponIdAndMemberId(final Long couponId, final Long memberId) {
        final String sql = "SELECT * FROM member_coupon WHERE coupon_id = ? AND member_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, couponId, memberId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(final Coupon usedCoupon, final Long memberId) {
        final String memberCouponSql = "UPDATE member_coupon SET is_used = ? WHERE coupon_id = ? AND member_id = ?";
        jdbcTemplate.update(memberCouponSql, usedCoupon.isUsed(), usedCoupon.getId(), memberId);
    }
}
