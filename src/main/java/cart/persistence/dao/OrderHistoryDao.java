package cart.persistence.dao;

import cart.persistence.entity.OrderHistoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderHistoryDao {

    private static final RowMapper<OrderHistoryEntity> ORDER_HISTORY_ENTITY_ROW_MAPPER = (rs, rowNum) -> new OrderHistoryEntity(
            rs.getLong("id"),
            null,
            rs.getInt("totalAmount"),
            rs.getInt("used_point"),
            rs.getInt("saved_point")
    );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public OrderHistoryDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_history")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrder(final OrderHistoryEntity orderHistoryEntity) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("member_id", orderHistoryEntity.getMemberId())
                .addValue("total_amount", orderHistoryEntity.getTotalAmount())
                .addValue("used_point", orderHistoryEntity.getUsedPoint())
                .addValue("saved_point", orderHistoryEntity.getSavedPoint());
        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public List<OrderHistoryEntity> getHistoriesByMemberId(final Long memberId) {
        final String sql = "SELECT id, total_amount, used_point, saved_point FROM order_history WHERE member_id = ?";
        return jdbcTemplate.query(sql, ORDER_HISTORY_ENTITY_ROW_MAPPER, memberId);
    }
}
