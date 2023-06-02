package cart.coupon.dao;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.EmptyCoupon;
import cart.coupon.domain.FixDiscountCoupon;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
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

  public List<Coupon> findByIdsIn2(final List<Long> couponIds) {
    final String sql = "SELECT * FROM COUPON C WHERE C.id IN (:ids)";

    final MapSqlParameterSource parameterSource =
        new MapSqlParameterSource().addValue("ids", couponIds);

    return namedParameterJdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> {
      final long id = rs.getLong("id");
      final String name = rs.getString("name");
      final BigDecimal discountPrice = rs.getBigDecimal("discount_price");
      return new FixDiscountCoupon(id, name, new Money(discountPrice));
    });
  }

  public Coupon findById(final Long couponId) {
    final String sql = "SELECT * FROM COUPON C WHERE C.id = ?";

    try {
      return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final BigDecimal discountPrice = rs.getBigDecimal("discount_price");
        return new FixDiscountCoupon(id, name, new Money(discountPrice));
      }, couponId);
    } catch (EmptyResultDataAccessException e) {
      return new EmptyCoupon();
    }
  }
}
