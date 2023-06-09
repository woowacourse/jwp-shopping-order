package cart.dao;

import cart.domain.Order;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Order> rowMapper = (resultSet, rowNumber) -> new Order(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getInt("actual_price"),
            resultSet.getInt("original_price"),
            resultSet.getInt("delivery_fee")
    );

    public OrdersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingColumns("member_id", "actual_price", "original_price", "delivery_fee")
                .usingGeneratedKeyColumns("id");
    }

    public long save(Order orderToCreate) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderToCreate);
        return this.simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<Order> findByMemberId(long memberId) {
        String sql = "SELECT id, member_id, actual_price, original_price, delivery_fee FROM orders WHERE member_id = ?";
        return this.jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Order findById(Long orderId) {
        String sql = "SELECT id, member_id, actual_price, original_price, delivery_fee FROM orders WHERE id = ?";
        return this.jdbcTemplate.queryForObject(sql, rowMapper, orderId);
    }
}
