package cart.dao;

import cart.domain.Order;
import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id");
    }

    public Long save(final Order order) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("member_id", order.getMember().getId());
        return jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public OrderEntity findById(final Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, orderEntityRowMapper(), id);
    }

    public RowMapper<OrderEntity> orderEntityRowMapper() {
        return (rs, rowNum) -> new OrderEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}
