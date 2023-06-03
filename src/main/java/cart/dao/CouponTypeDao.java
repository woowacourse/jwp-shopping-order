package cart.dao;

import cart.dao.entity.CouponTypeEntity;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponTypeDao {

    private static final RowMapper<CouponTypeEntity> ROW_MAPPER = (resultSet, rowNum) -> new CouponTypeEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("discount_type"),
            resultSet.getLong("discount_amount")
    );

    private final JdbcTemplate jdbcTemplate;

    public CouponTypeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CouponTypeEntity> findById(final Long id) {
        final String sql = "SELECT id, name, discount_type, discount_amount "
                + "FROM coupon_type "
                + "WHERE id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}

