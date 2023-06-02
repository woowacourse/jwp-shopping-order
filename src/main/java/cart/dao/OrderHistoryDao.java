package cart.dao;

import cart.domain.OrderHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderHistoryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(OrderHistory orderHistory) {
        String sql = "INSERT INTO order_history (member_id, original_price, used_point, order_price) " +
                "VALUES (:memberId, :originalPrice, :usedPoint, :orderPrice)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("memberId", orderHistory.getMember().getId());
        params.addValue("originalPrice", orderHistory.getOriginalPrice());
        params.addValue("usedPoint", orderHistory.getUsedPoint());
        params.addValue("orderPrice", orderHistory.getOrderPrice());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
