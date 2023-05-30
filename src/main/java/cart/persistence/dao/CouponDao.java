package cart.persistence.dao;

import cart.persistence.entity.CouponEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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

    private final RowMapper<CouponEntity> couponEntityRowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final int discountRate = rs.getInt("discount_rate");
        final int period = rs.getInt("period");
        final LocalDateTime expiredDate = rs.getTimestamp("expired_date").toLocalDateTime();
        return new CouponEntity(id, name, discountRate, period, expiredDate);
    };

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CouponEntity> getAllCoupons() {
        String sql = "SELECT * FROM coupon";
        return jdbcTemplate.query(sql, couponEntityRowMapper);
    }

    public Optional<CouponEntity> findById(final Long couponId) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, couponEntityRowMapper, couponId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Long insert(final CouponEntity coupon) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO coupon (name, discount_rate, period, expired_date) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, coupon.getName());
            ps.setInt(2, coupon.getDiscountRate());
            ps.setInt(3, coupon.getPeriod());
            ps.setTimestamp(4, Timestamp.valueOf(coupon.getExpiredDate()));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int deleteById(final Long couponId) {
        final String sql = "DELETE FROM coupon WHERE id = ?";
        return jdbcTemplate.update(sql, couponId);
    }

    public boolean existByNameAndDiscountRate(final String name, final int discountRate) {
        final String sql = "SELECT COUNT(*) FROM coupon WHERE name = ? and discount_rate = ?";
        final long count = jdbcTemplate.queryForObject(sql, Long.class, name, discountRate);
        return count > 0;
    }

    public Optional<CouponEntity> findByName(final String name) {
        String sql = "SELECT * FROM coupon WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, couponEntityRowMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
