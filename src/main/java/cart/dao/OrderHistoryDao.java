package cart.dao;

import cart.domain.Order;
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

    public Long createOrder(final Order order) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("member_id", order.getMember().getId())
                .addValue("total_amount", order.getTotalAmount())
                .addValue("used_point", order.getUsedPoint())
                .addValue("saved_point", order.getSavedPoint());
        return jdbcInsert.executeAndReturnKey(source).longValue();
    }
}
