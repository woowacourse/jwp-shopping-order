package cart.dao;

import cart.repository.dto.OrderEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
        long orderId = rs.getLong("id");
        long memberId = rs.getLong("member_id");
        long spendPoint = rs.getLong("spend_point");
        LocalDateTime orderCreatedAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new OrderEntity(orderId, memberId, orderCreatedAt, spendPoint);
    };

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(OrderEntity orderEntity) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("member_id", orderEntity.getMemberId());
        mapSqlParameterSource.addValue("spend_point", orderEntity.getSpendPoint());
        mapSqlParameterSource.addValue("created_at", orderEntity.getCreatedAt());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    public Optional<OrderEntity> findById(Long orderId) {
        String sql = "SELECT id, created_at, spend_point, member_id " +
                "FROM orders " +
                "WHERE id = ?";

        return jdbcTemplate.query(sql, rowMapper, orderId).stream().findAny();
    }
}
