package cart.dao;

import cart.domain.MemberCoupon;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(MemberCoupon memberCoupon) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", memberCoupon.getMemberId());
        parameters.put("coupon_id", memberCoupon.getCoupon().getId());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
