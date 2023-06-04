package cart.coupon.dao;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.EmptyCoupon;
import cart.coupon.domain.FixDiscountCoupon;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

  private static final long NOT_USED_WITH_ORDER_COUPON_ID = 0L;

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public CouponDao(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
  }

  public List<Coupon> findByIdsIn(final List<Long> couponIds) {
    final String sql = "SELECT * FROM COUPON C WHERE C.id IN (:ids)";

    final MapSqlParameterSource parameterSource =
        new MapSqlParameterSource().addValue("ids", couponIds);

    return namedParameterJdbcTemplate.query(sql, parameterSource, getRowMapper());
  }

  private static RowMapper<Coupon> getRowMapper() {
    return (rs, rowNum) -> {
      final long id = rs.getLong("id");
      final String name = rs.getString("name");
      final BigDecimal discountPrice = rs.getBigDecimal("discount_price");
      return new FixDiscountCoupon(id, name, new Money(discountPrice));
    };
  }

  public Optional<Coupon> findById(final Long couponId) {
    if (isNotUsingCouponInOrder(couponId)) {
      return Optional.of(new EmptyCoupon());
    }

    final String sql = "SELECT * FROM COUPON C WHERE C.id = ?";

    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getRowMapper(), couponId));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  private boolean isNotUsingCouponInOrder(final Long couponId) {
    return couponId == null || couponId.equals(NOT_USED_WITH_ORDER_COUPON_ID);
  }
}
