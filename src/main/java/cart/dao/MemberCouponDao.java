package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.member.MemberCoupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    private static RowMapper<MemberCoupon> getCouponRowMapper() {
        return (rs, rowNum) -> new MemberCoupon(rs.getLong("member_coupon.id"),
                new Coupon(
                        rs.getLong("coupon.id"),
                        rs.getString("name"),
                        new Discount(rs.getString("discount_type"), rs.getInt("amount"))
                ));
    }

    public List<MemberCoupon> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM coupon INNER JOIN member_coupon ON coupon.id = member_coupon.coupon_id WHERE member_coupon.member_id = ?";
        return jdbcTemplate.query(sql, getCouponRowMapper(), memberId);
    }

    public List<MemberCoupon> findByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT * FROM coupon INNER JOIN member_coupon ON coupon.id = member_coupon.coupon_id WHERE member_coupon.id IN (:ids)";

        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedJdbcTemplate.query(sql, parameters, getCouponRowMapper());
    }

    public Long create(Long memberId, Long couponId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", memberId)
                .addValue("coupon_id", couponId);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void delete(List<Long> ids) {
        final String sql = "delete from member_coupon where id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        namedJdbcTemplate.update(sql, parameters);
    }
}
