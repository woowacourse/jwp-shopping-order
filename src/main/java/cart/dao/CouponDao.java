package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.Coupon;
import cart.domain.CouponType;

@Repository
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Coupon findById(Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[] {couponId}, rs -> {
            if (rs.next()) {
                String name = rs.getString("name");
                int minPrice = rs.getInt("min_order_price");
                CouponType couponType = CouponType.valueOf(rs.getString("type"));
                int discountAmount = rs.getInt("discount_amount");
                double discountPercentage = rs.getDouble("discount_percentage");
                int maxPrice = rs.getInt("max_discount_price");
                return new Coupon(couponId, name, minPrice, couponType, discountAmount, discountPercentage, maxPrice);
            }
            return null;
        });
    }

    public long save(Coupon coupon) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO coupon (name, min_order_price, max_discount_price, type, discount_amount, discount_percentage) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, coupon.getName());
            ps.setObject(2, coupon.getMinOrderPrice());
            ps.setObject(3, coupon.getMaxDiscountPrice());
            ps.setString(4, coupon.getType().name());
            ps.setObject(5, coupon.getDiscountAmount());
            ps.setObject(6, coupon.getDiscountPercentage());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<Coupon> findAll() {
        String query = "SELECT * FROM coupon";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        return rows.stream()
            .map(row -> {
                Long id = (Long)row.get("id");
                String name = (String)row.get("name");
                Integer minOrderPrice = (Integer)row.get("min_order_price");
                Integer maxDiscountPrice = (Integer)row.get("max_discount_price");
                CouponType type = CouponType.valueOf((String)row.get("type"));
                Integer discountAmount = (Integer)row.get("discount_amount");
                Double discountPercentage = (Double)row.get("discount_percentage");
                return new Coupon(id, name, minOrderPrice, type, discountAmount, discountPercentage, maxDiscountPrice);
            })
            .collect(Collectors.toUnmodifiableList());
    }
}
