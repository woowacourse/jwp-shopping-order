package cart.coupon.infrastructure.dao;

import cart.common.annotation.Dao;
import cart.coupon.infrastructure.entity.CouponEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Dao
public class CouponDao {

    private static final RowMapper<CouponEntity> couponRowMapper =
            new BeanPropertyRowMapper<>(CouponEntity.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(CouponEntity coupon) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(coupon);
        return simpleJdbcInsert.executeAndReturnKey(source)
                .longValue();
    }

    public Optional<CouponEntity> findById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, couponRowMapper, id)
        );
    }

    public List<CouponEntity> findAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM coupon WHERE member_id = ?";
        return jdbcTemplate.query(sql, couponRowMapper, memberId);
    }

    public List<CouponEntity> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "SELECT * FROM coupon WHERE id IN (:ids)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, source, couponRowMapper);
    }
}
