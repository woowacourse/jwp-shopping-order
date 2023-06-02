package cart.member_coupon.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) ->
      new MemberCouponEntity(
          rs.getLong("member_id"),
          rs.getLong("coupon_id")
      );


  public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<MemberCouponEntity> findByMemberId(final Long id) {
    final String sql = "SELECT * FROM MEMBER_COUPON MC WHERE MC.member_id = ? and MC.used_yn = ?";

    return jdbcTemplate.query(sql, rowMapper, id, "N");
  }
}
