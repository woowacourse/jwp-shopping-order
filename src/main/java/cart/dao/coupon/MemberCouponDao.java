package cart.dao.coupon;

import cart.entity.coupon.MemberCouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<MemberCouponEntity> rowMapper = (rs, rowNum) ->
            new MemberCouponEntity(
                    rs.getLong("id"),
                    rs.getLong("coupon_id"),
                    rs.getLong("member_id")
            );

    public List<MemberCouponEntity> findAllByMemberId(final long memberId) {
        String sql = "SELECT * FROM member_coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public long save(final long memberId, final long couponId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("member_id", memberId);
        parameters.put("coupon_id", couponId);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }
}
