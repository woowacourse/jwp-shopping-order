package cart.coupon.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) ->
      new CouponEntity(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getBigDecimal("discount_price")
      );

  public CouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }


  public List<CouponEntity> findByIdsIn(final List<Long> couponIds) {
    final String sql = "SELECT * FROM COUPON C WHERE C.id IN (:ids)";

    final MapSqlParameterSource parameterSource =
        new MapSqlParameterSource().addValue("ids", couponIds);

    return namedParameterJdbcTemplate.query(sql, parameterSource, rowMapper);
  }
}
