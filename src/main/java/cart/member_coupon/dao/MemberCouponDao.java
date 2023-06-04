package cart.member_coupon.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) ->
      new MemberCouponEntity(
          rs.getLong("member_id"),
          rs.getLong("coupon_id"),
          rs.getString("used_yn")
      );


  public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<MemberCouponEntity> findByMemberId(final Long id) {
    final String sql = "SELECT * FROM MEMBER_COUPON MC WHERE MC.member_id = ? and MC.used_yn = ?";

    return jdbcTemplate.query(sql, rowMapper, id, "N");
  }

  public void updateMemberCoupon(final Long couponId, final Long memberId, final String usedYn) {
    final String sql = "UPDATE MEMBER_COUPON MC SET used_yn = ? WHERE MC.coupon_id = ? AND MC.member_id = ?";

    jdbcTemplate.update(sql, usedYn, couponId, memberId);
  }

  public Optional<MemberCouponEntity> findByMemberAndCouponId(
      final Long couponId,
      final Long memberId
  ) {
    final String sql = "SELECT * FROM MEMBER_COUPON MC WHERE MC.member_id = ? AND MC.coupon_id = ?";

    try {
      return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, memberId, couponId));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
