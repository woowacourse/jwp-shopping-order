package cart.persistence.coupon;

import cart.application.repository.CouponRepository;
import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.AmountCoupon;
import cart.domain.discountpolicy.CouponPolicy;
import cart.domain.discountpolicy.PercentCoupon;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private static final String USABLE = "1";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleOrderedCouponInsert;

    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) ->
            new Coupon(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("discount_percent"),
                    rs.getInt("discount_amount"),
                    rs.getInt("min_amount")
            );

    private final RowMapper<CouponPolicy> percentCouponRowMapper = (rs, rowMapper) ->
            new PercentCoupon(
                    rs.getInt("coupon.min_amount"),
                    rs.getInt("coupon.discount_percent")
            );

    private final RowMapper<CouponPolicy> amountCouponRowMapper = (rs, rowMapper) ->
            new AmountCoupon(
                    rs.getInt("coupon.min_amount"),
                    rs.getInt("coupon.discount_amount")
            );

    public CouponJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleOrderedCouponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ordered_coupon")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Coupon> findByMemberId(final Long memberId) {
        final String sql = "SELECT coupon.* " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.member_id = ? AND member_coupon.status = " + USABLE;
        return jdbcTemplate.query(sql, couponRowMapper, memberId);
    }

    @Override
    public Optional<CouponPolicy> findPercentCouponById(final Long memberCouponId) {
        final String sql = "SELECT coupon.min_amount, coupon.discount_percent " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.id = ? AND discount_amount = 0 AND member_coupon.status = 1";

        try {
            CouponPolicy percentCoupon = jdbcTemplate.queryForObject(sql, percentCouponRowMapper, memberCouponId);
            return Optional.of(percentCoupon);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CouponPolicy> findAmountCouponById(final Long memberCouponId) {
        final String sql = "SELECT coupon.min_amount, coupon.discount_amount " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.id = ? AND discount_percent = 0 AND member_coupon.status = 1";

        try {
            CouponPolicy percentCoupon = jdbcTemplate.queryForObject(sql, amountCouponRowMapper, memberCouponId);
            return Optional.of(percentCoupon);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void convertToUseMemberCoupon(final Long memberCouponId) {
        String sql = "UPDATE member_coupon SET status = 0 WHERE id = ?";
        jdbcTemplate.update(sql, memberCouponId);
    }

    @Override
    public long createOrderedCoupon(final Long orderId, final Long memberCouponId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("order_id", orderId);
        parameters.addValue("member_coupon_id", memberCouponId);

        return simpleOrderedCouponInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Coupon> findUsedCouponByOrderId(Long orderId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.min_amount, coupon.discount_percent, coupon.discount_amount " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "JOIN ordered_coupon ON member_coupon.id = ordered_coupon.member_coupon_id " +
                "WHERE ordered_coupon.order_id = ?";

        return jdbcTemplate.query(sql, couponRowMapper, orderId);
    }
}
