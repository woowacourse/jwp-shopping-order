package cart.dao;

import cart.dao.dto.CouponDto;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CouponDao(final DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public CouponDto insert(final CouponDto couponDto) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("name", couponDto.getName())
                .addValue("value", couponDto.getValue())
                .addValue("coupon_type", couponDto.getCouponType());
        final long couponId = jdbcInsert.executeAndReturnKey(source).longValue();
        return new CouponDto(couponId, couponDto.getName(), couponDto.getValue(), couponDto.getCouponType());
    }

    public CouponDto findById(final Long id) {
        final String sql = "select * from Coupon where id = :id";
        return jdbcTemplate.queryForObject(sql, Map.of("id", id), CouponDto.class);
    }
}
