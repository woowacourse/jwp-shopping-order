package cart.dao;

import cart.dao.entity.OrderEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderEntity> ROW_MAPPER = (resultSet, rowNum) -> {
        Long couponId = resultSet.getLong("coupon_id");
        if (resultSet.wasNull()) {
            couponId = null;
        }
        return new OrderEntity(
                resultSet.getLong("id"),
                resultSet.getLong("member_id"),
                couponId,
                resultSet.getLong("delivery_fee"),
                resultSet.getString("status"),
                resultSet.getTimestamp("created_at")
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "coupon_id", "delivery_fee", "status");
    }

    public Long save(final OrderEntity orderEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderEntity))
                .longValue();
    }

    public List<OrderEntity> findByMemberId(final long memberId) {
        final String sql = "SELECT id, coupon_id, member_id, delivery_fee, status, created_at "
                + "FROM orders "
                + "WHERE member_id = ? "
                + "ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public Optional<OrderEntity> findById(final Long id) {
        final String sql = "SELECT id, coupon_id, member_id, delivery_fee, status, created_at "
                + "FROM orders "
                + "WHERE id = ? "
                + "ORDER BY created_at DESC";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM orders WHERE id = ? ";
        jdbcTemplate.update(sql, id);
    }

    public void updateStatus(final OrderEntity order) {
        final String sql = "UPDATE orders "
                + "SET status = ? "
                + "WHERE id = ? ";
        jdbcTemplate.update(sql, order.getStatus(), order.getId());
    }
}
