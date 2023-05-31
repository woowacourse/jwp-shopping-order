package cart.dao;

import cart.dto.MemberCouponDto;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberCouponDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(MemberCouponDto memberCouponDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", memberCouponDto.getMemberId());
        params.put("coupon_id", memberCouponDto.getCouponId());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

}
