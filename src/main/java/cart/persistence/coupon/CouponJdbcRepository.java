package cart.persistence.coupon;

import cart.application.coupon.dto.MemberCouponDto;
import cart.domain.coupon.Coupon;
import cart.domain.repository.coupon.CouponRepository;
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
                    rs.getLong("coupon.id"),
                    rs.getString("coupon.name"),
                    rs.getInt("coupon.discount_percent"),
                    rs.getInt("coupon.discount_amount"),
                    rs.getInt("coupon.min_amount")
            );

    private final RowMapper<MemberCouponDto> memberCouponDtoRowMapper = ((rs, rowNum) ->
            new MemberCouponDto(
                    rs.getLong("member_coupon.id"),
                    rs.getString("coupon.name"),
                    rs.getInt("coupon.discount_percent"),
                    rs.getInt("coupon.discount_amount"),
                    rs.getInt("coupon.min_amount")
            ));

    public CouponJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleOrderedCouponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ordered_coupon")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Coupon> findUsableCouponByMemberCouponId(Long memberCouponId) {
        String sql = "SELECT coupon.id, coupon.name, coupon.min_amount, coupon.discount_percent, coupon.discount_amount " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.id = ? AND member_coupon.status = 1";
        try {
            Coupon coupon = jdbcTemplate.queryForObject(sql, couponRowMapper, memberCouponId);
            return Optional.of(coupon);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MemberCouponDto> findByMemberId(Long memberId) {
        String sql = "SELECT coupon.*, member_coupon.id " +
                "FROM coupon " +
                "JOIN member_coupon ON coupon.id = member_coupon.coupon_id " +
                "WHERE member_coupon.member_id = ? AND member_coupon.status = " + USABLE;
        return jdbcTemplate.query(sql, memberCouponDtoRowMapper, memberId);
    }

    @Override
    public void convertToUseMemberCoupon(Long memberCouponId) {
        String sql = "UPDATE member_coupon SET status = 0 WHERE id = ?";
        jdbcTemplate.update(sql, memberCouponId);
    }

    @Override
    public long createOrderedCoupon(Long orderId, Long memberCouponId) {
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
