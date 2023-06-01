package cart.dao;

import cart.dao.dto.CouponDto;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private static final RowMapper<CouponDto> COUPON_DTO_ROW_MAPPER = (rs, rn) -> {
        final Long couponId = rs.getLong("id");
        final String name = rs.getString("name");
        final Integer value = rs.getInt("val");
        final String couponType = rs.getString("coupon_type");
        return new CouponDto(couponId, name, value, couponType);
    };
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
                .addValue("val", couponDto.getValue())
                .addValue("coupon_type", couponDto.getCouponType());
        final long couponId = jdbcInsert.executeAndReturnKey(source).longValue();
        return new CouponDto(couponId, couponDto.getName(), couponDto.getValue(), couponDto.getCouponType());
    }

    public CouponDto findById(final Long id) {
        final String sql = "select * from coupon where id = :id";
        return jdbcTemplate.queryForObject(sql, Map.of("id", id), COUPON_DTO_ROW_MAPPER);
    }

    public List<CouponDto> findByIds(final Collection<Long> ids) {
        final String sql = "select * from coupon where id IN (:ids)";
        return jdbcTemplate.query(sql, Map.of("ids", ids), COUPON_DTO_ROW_MAPPER);
    }

    public List<CouponDto> findByMemberId(final Long id) {
        final String sql = "select c.id id, c.name name, c.val val, c.coupon_type coupon_type from member_coupon mc"
                + "inner join coupon c"
                + "on mc.member_id = c.id"
                + "where mc.member_id = " + id;
        return jdbcTemplate.query(sql, COUPON_DTO_ROW_MAPPER);
    }
}
