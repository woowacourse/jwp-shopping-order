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

    private static final RowMapper<OrderEntity> ROW_MAPPER = (resultSet, rowNum) -> new OrderEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getLong("delivery_fee")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "delivery_fee");
    }

    public Long save(final OrderEntity orderEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(orderEntity))
                .longValue();
    }

    public List<OrderEntity> findByMemberId(final long memberId) {
        final String sql = "SELECT id, member_id, delivery_fee "
                + "FROM orders "
                + "WHERE member_id = ? "
                + "ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public Optional<OrderEntity> find(final Long memberId, final Long orderId) {
        final String sql = "SELECT id, member_id, delivery_fee "
                + "FROM orders "
                + "WHERE id = ? "
                + "AND member_id = ? "
                + "ORDER BY created_at DESC";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, orderId, memberId));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long orderId) {
        final String sql = "DELETE FROM orders WHERE id = ? ";
        jdbcTemplate.update(sql, orderId);
    }
}
