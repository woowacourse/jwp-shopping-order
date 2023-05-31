package cart.dao;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private static final RowMapper<Coupon> rowMapper = (rs, rowNum) -> new Coupon(
            rs.getLong("id"),
            rs.getString("name"),
            CouponType.from(rs.getString("type")),
            rs.getInt("discount_amount")
    );

    private static final RowMapper<CouponEntity> entityRowMapper = (rs, rowNum) -> new CouponEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getInt("discount_amount")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long save(Coupon coupon) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(coupon);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<Coupon> findById(Long id) {
        String sql = "select * from coupon where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Coupon> findAll() {
        String sql = "select * from coupon";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Coupon> findAllByMemberId(Long memberId) {
        String sql = "select coupon.id, coupon.name, coupon.type, coupon.discount_amount, member_coupon.member_id " +
                "from coupon " +
                "join member_coupon on coupon.id = member_coupon.coupon_id " +
                "where member_coupon.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<CouponEntity> findAllByIds(Set<Long> ids) {
        String sql = "SELECT * FROM coupon WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, entityRowMapper);
    }
}
