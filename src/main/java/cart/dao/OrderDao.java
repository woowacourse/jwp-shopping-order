package cart.dao;

import cart.entity.OrderEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) ->
            new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("original_price"),
                    rs.getInt("discount_price"),
                    rs.getBoolean("confirm_state")
            );

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql = "select * from orders where member_id = ?";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long saveOrder(OrderEntity orderEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(orderEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<OrderEntity> findByOrderId(Long memberId, Long orderId) {
        try {
            String sql = "select * from orders where id = ? and member_id = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, orderId, memberId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteOrderById(Long orderId) {
        String sql = "delete from orders where id = ?";

        jdbcTemplate.update(sql,orderId);
    }
}
