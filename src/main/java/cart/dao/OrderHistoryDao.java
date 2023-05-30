package cart.dao;

import cart.entity.OrderHistoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderHistoryDao {

    private final SimpleJdbcInsert insertOrderHistory;

    public OrderHistoryDao(final JdbcTemplate jdbcTemplate) {
        this.insertOrderHistory = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_history")
                .usingGeneratedKeyColumns("id");
    }

    public OrderHistoryEntity insert(final OrderHistoryEntity entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final long id = insertOrderHistory.executeAndReturnKey(parameters).longValue();
        return new OrderHistoryEntity(id, entity.getMemberId(), entity.getOrderPrice(), entity.getOrderPrice());
    }
}
