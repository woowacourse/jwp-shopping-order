package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("orders");
    }

    public Optional<OrderEntity> getById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            jdbcTemplate.queryForObject(sql, new OrdersEntityMapper(), id);
            OrderEntity orderEntity = jdbcTemplate.queryForObject(sql, new OrdersEntityMapper());
            return Optional.ofNullable(orderEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> getAllByMemberId(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, new OrdersEntityMapper(), memberId);
    }

    public Long insert(OrderEntity entity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    private static class OrdersEntityMapper implements RowMapper<OrderEntity> {
        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("used_money"),
                    rs.getInt("used_point"),
                    rs.getTimestamp("created_at")
            );
        }
    }
}
