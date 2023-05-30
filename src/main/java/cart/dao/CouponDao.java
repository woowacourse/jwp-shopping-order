package cart.dao;

import cart.dao.entity.CouponEntity;
import cart.domain.Coupon;
import cart.domain.CouponType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<CouponEntity> couponEntityRowMapper = (rs, rowNum) -> new CouponEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("discount_type"),
            rs.getFloat("discount_rate"),
            rs.getInt("discount_amount"),
            rs.getInt("minimum_price")
    );

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Coupon findWithId(final long id) {
        final String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Coupon(
                rs.getLong("id"),
                rs.getString("name"),
                CouponType.mappingType(rs.getString("discount_type")),
                rs.getFloat("discount_rate"),
                rs.getInt("discount_amount"),
                rs.getInt("minimum_price")
        ), id);
    }

    public List<CouponEntity> findAll() {
        final String sql = "SELECT * FROM coupon";
        return jdbcTemplate.query(sql, couponEntityRowMapper);
    }
}
