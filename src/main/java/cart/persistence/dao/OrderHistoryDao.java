package cart.persistence.dao;

import cart.persistence.entity.OrderHistoryEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderHistoryDao {

    private final SimpleJdbcInsert jdbcInsert;

    public OrderHistoryDao(final JdbcTemplate jdbcTemplate) {
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
}
