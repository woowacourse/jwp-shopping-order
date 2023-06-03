package cart.persistence.coupon;

import cart.application.repository.coupon.CouponRepository;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private static final String USABLE = "1";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleOrderedCouponInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) ->
            new Coupon(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("min_amount"),
                    rs.getInt("discount_percent"),
                    rs.getInt("discount_amount")
            );

    public CouponJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleOrderedCouponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ordered_coupon")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Coupons findByMemberId(final Long memberId) {
        final String sql = "SELECT coupon.* " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.member_id = ? AND member_coupon.status = " + USABLE;
        return new Coupons(jdbcTemplate.query(sql, couponRowMapper, memberId));
    }

    public Coupons findMemberCouponByCouponIds(final Long memberId, final List<Long> couponIds) {
        final String sql = "SELECT coupon.* " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.member_id = :memberId AND member_coupon.coupon_id IN (:couponIds) AND member_coupon.status = :status";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("memberId", memberId);
        parameters.addValue("couponIds", couponIds);
        parameters.addValue("status", USABLE);

        return new Coupons(namedParameterJdbcTemplate.query(sql, parameters, couponRowMapper));
    }

    @Override
    public void convertToUseMemberCoupon(final Coupon coupon) {
        String sql = "UPDATE member_coupon SET status = 0 WHERE id = ?";
        jdbcTemplate.update(sql, coupon.getId());
    }

    @Override
    public void createOrderedCoupon(final Long orderId, final Coupon coupon) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("order_id", orderId);
        parameters.addValue("member_coupon_id", coupon.getId());

        simpleOrderedCouponInsert.execute(parameters);
    }

    @Override
    public Coupons findCouponByOrderId(final Long memberId, final Long orderId) {
        String sql = "SELECT c.id, c.name, c.min_amount, c.discount_percent, c.discount_amount" +
                "FROM member_coupon mc" +
                "JOIN coupon c ON mc.coupon_id = c.id" +
                "JOIN ordered_coupon oc ON mc.id = oc.member_coupon_id" +
                "WHERE mc.member_id = ? AND oc.order_id = ? AND mc.status = 1;";
        return new Coupons(jdbcTemplate.query(sql, couponRowMapper, memberId, orderId));
    }

}
