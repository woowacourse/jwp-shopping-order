package cart.persistence.dao;

import cart.persistence.entity.CouponEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingColumns("name", "discount_type", "discount_percent", "discount_amount", "minimum_price")
                .usingGeneratedKeyColumns("id");
    }

    public List<CouponEntity> findAll() {
        String sql = "SELECT * from coupon";
        return jdbcTemplate.query(sql, new CouponEntityRowMapper());
    }

    public Optional<CouponEntity> findById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        List<CouponEntity> coupons = jdbcTemplate.query(sql, new Object[]{id}, new CouponEntityRowMapper());
        return coupons.isEmpty() ? Optional.empty() : Optional.ofNullable(coupons.get(0));
    }

    public Long add(CouponEntity coupon) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(coupon);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM coupon WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class CouponEntityRowMapper implements RowMapper<CouponEntity> {
        @Override
        public CouponEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("discount_type"),
                    rs.getDouble("discount_percent"),
                    rs.getInt("discount_amount"),
                    rs.getInt("minimum_price")
            );
        }
    }
}
