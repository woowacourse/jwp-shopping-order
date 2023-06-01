package cart.dao;

import cart.dto.CouponDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
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
            rs.getInt("discount_price"));

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
        params.put("discount_price", couponDto.getDiscountPrice());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<CouponDto> findById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, couponDtoRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

}
