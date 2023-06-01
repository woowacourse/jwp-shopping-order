package cart.dao;

import cart.entity.CouponEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class CouponDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<CouponEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String policyType = rs.getString("policy_type");
        final long disCountPrice = rs.getLong("discount_value");
        final long minimumPrice = rs.getLong("minimum_price");
        return new CouponEntity(id, name, policyType, disCountPrice, minimumPrice);
    };

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingColumns("name", "policy_type", "discount_value", "minimum_price")
                .usingGeneratedKeyColumns("id");
    }

    public CouponEntity insert(final CouponEntity couponEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(couponEntity);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new CouponEntity(
                id,
                couponEntity.getName(),
                couponEntity.getPolicyType(),
                couponEntity.getDiscountValue(),
                couponEntity.getMinimumPrice()
        );
    }

    @Transactional(readOnly = true)
    public List<CouponEntity> findByIds(final List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "SELECT * FROM coupon WHERE id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedJdbcTemplate.query(sql, parameters, rowMapper);
    }

    @Transactional(readOnly = true)
    public Optional<CouponEntity> findById(final Long id) {
        final String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
