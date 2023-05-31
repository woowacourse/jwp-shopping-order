package cart.dao;

import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final RowMapper<CouponDto> couponDtoRowMapper = (rs, rn) -> new CouponDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDouble("discount_rate"),
            rs.getInt("discount_charge"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CouponDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(CouponDto couponDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", couponDto.getName());
        params.put("discount_rate", couponDto.getDiscountRate());
        params.put("discount_charge", couponDto.getDiscountCharge());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

}
