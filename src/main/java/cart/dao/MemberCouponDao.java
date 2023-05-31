package cart.dao;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.MemberCoupon;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<MemberCoupon> rowMapper = (rs, rowNum) -> new MemberCoupon(
            rs.getLong("id"),
            rs.getLong("member_id"),
            new Coupon(
                    rs.getLong("coupon.id"),
                    rs.getString("coupon.name"),
                    CouponType.from(rs.getString("coupon.type")),
                    rs.getInt("coupon.discount_amount")
            ));


    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long save(MemberCoupon memberCoupon) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", memberCoupon.getMemberId());
        parameters.put("coupon_id", memberCoupon.getCoupon().getId());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public List<MemberCoupon> findAllByIds(Set<Long> ids) {
        String sql = "SELECT * FROM member_coupon WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, rowMapper);
    }
}
