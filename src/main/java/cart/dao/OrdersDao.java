package cart.dao;

import cart.domain.Orders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    public OrdersDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrders(final long memberId, final int originalPrice, final  int discountPrice){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id",memberId)
                .addValue("original_price",originalPrice)
                .addValue("discount_price",discountPrice)
                .addValue("confirm_state",false);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }
}
