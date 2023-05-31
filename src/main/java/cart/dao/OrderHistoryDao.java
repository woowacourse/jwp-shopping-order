package cart.dao;

import cart.entity.OrderHistoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderHistoryDao {

    private static final RowMapper<OrderHistoryEntity> ORDER_HISTORY_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new OrderHistoryEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getInt("used_point"),
            resultSet.getInt("order_price")
    );
    private final SimpleJdbcInsert insertOrderHistory;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderHistoryDao(final JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertOrderHistory = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_history")
                .usingGeneratedKeyColumns("id");
    }

    public OrderHistoryEntity insert(final OrderHistoryEntity entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final long id = insertOrderHistory.executeAndReturnKey(parameters).longValue();
        return new OrderHistoryEntity(id, entity.getMemberId(), entity.getOrderPrice(), entity.getOrderPrice());
    }

    public List<OrderHistoryEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM order_history WHERE member_id = :member_id";
        final Map<String, Long> parameter = Map.of("member_id", memberId);
        return namedParameterJdbcTemplate.query(sql, parameter, ORDER_HISTORY_ENTITY_ROW_MAPPER);
    }
}
