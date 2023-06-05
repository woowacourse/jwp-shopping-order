package cart.dao;

import cart.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

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
}
