package shop.persistence.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import shop.exception.DatabaseException;
import shop.persistence.entity.CouponEntity;

import java.util.List;
import java.util.Optional;

@Component
public class CouponDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<CouponEntity> rowMapper =
            (rs, rowNum) -> new CouponEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("discount_rate"),
                    rs.getInt("period"),
                    rs.getTimestamp("expired_at").toLocalDateTime()
            );

    public CouponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(CouponEntity couponEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(couponEntity);

        try {
            return simpleJdbcInsert.executeAndReturnKey(param).longValue();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException.DataConflictException("동일한 이름(" + couponEntity.getName() + ")과 " +
                    "할인율(" + couponEntity.getDiscountRate() + ")을 가진 쿠폰이 존재합니다.");
        }
    }

    public List<CouponEntity> findAll() {
        String sql = "SELECT * FROM coupon";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public CouponEntity findById(Long id) {
        String sql = "SELECT * FROM coupon WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            throw new DatabaseException.IllegalDataException(id + "를 갖는 쿠폰을 찾을 수 없습니다.");
        }
    }

    public Optional<CouponEntity> findByNameAndDiscountRate(String name, Integer discountRate) {
        String sql = "SELECT * FROM coupon WHERE name = ? AND discount_rate = ?";
        CouponEntity couponEntity = jdbcTemplate.queryForObject(sql, rowMapper, name, discountRate);

        return Optional.ofNullable(couponEntity);
    }
}
