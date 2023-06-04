package cart.dao;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.MemberCoupon;
import cart.entity.MemberCouponEntity;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<MemberCoupon> memberCouponRowMapper = ((rs, rowNum) ->
            new MemberCoupon(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    new Coupon(
                            rs.getLong("coupon.id"),
                            rs.getString("coupon.name"),
                            CouponType.from(rs.getString("coupon.type")),
                            rs.getInt("coupon.discount_amount")
                    ),
                    rs.getBoolean("used")
            ));

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long save(MemberCouponEntity memberCouponEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(memberCouponEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public List<MemberCoupon> findUnusedByMemberId(Long memberId) {
        String sql = "SELECT mc.id, mc.member_id, mc.used, c.id, c.name, c.type, c.discount_amount " +
                "FROM member_coupon mc " +
                "JOIN coupon c ON mc.coupon_id = c.id " +
                "WHERE mc.member_id = ? and mc.used = ?";
        return jdbcTemplate.query(sql, memberCouponRowMapper, memberId, false);
    }

    public List<MemberCoupon> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT mc.id, mc.member_id, mc.used, c.id, c.name, c.type, c.discount_amount " +
                "FROM member_coupon mc " +
                "JOIN coupon c ON mc.coupon_id = c.id " +
                "WHERE mc.id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, memberCouponRowMapper);
    }

    public void uses(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }

        String sql = "UPDATE member_coupon SET used = true WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
