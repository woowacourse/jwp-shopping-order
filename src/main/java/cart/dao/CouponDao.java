package cart.dao;

import cart.dao.entity.CouponEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {

    private static final RowMapper<CouponEntity> ROW_MAPPER = (resultSet, rowNum) -> new CouponEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getLong("coupon_type_id"),
            resultSet.getBoolean("is_used")
    );

    private final JdbcTemplate jdbcTemplate;

    public CouponDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CouponEntity> findById(final Long id) {
        final String sql = "SELECT id, member_id, coupon_type_id, is_used "
                + "FROM coupon "
                + "WHERE id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void updateStatus(final CouponEntity coupon) {
        final String sql = "UPDATE coupon "
                + "SET is_used = ? "
                + "WHERE id = ? ";
        jdbcTemplate.update(sql, coupon.isUsed(), coupon.getId());
    }

    public List<CouponEntity> findByMember(final Long memberId) {
        final String sql = "SELECT id, member_id, coupon_type_id, is_used "
                + "FROM coupon "
                + "WHERE member_id = ? ";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    // TODO findByMember

    // TODO add, findAll, remove (admin)
}
