package cart.dao;

import cart.entity.CouponEntity;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private static final RowMapper<CouponEntity> ROW_MAPPER = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String type = rs.getString("type");
        BigDecimal discountValue = rs.getBigDecimal("discount_value");
        BigDecimal minOrderPrice = rs.getBigDecimal("min_order_price");
        return new CouponEntity(id, name, type, discountValue, minOrderPrice);
    };

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CouponEntity> findById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            CouponEntity couponEntity = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.ofNullable(couponEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(CouponEntity coupon) {
        String sql = "INSERT INTO coupon (name, type, discount_value, min_order_price) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, coupon.getName());
            ps.setString(2, coupon.getType());
            ps.setBigDecimal(3, coupon.getDiscountValue());
            ps.setBigDecimal(4, coupon.getMinOrderPrice());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
