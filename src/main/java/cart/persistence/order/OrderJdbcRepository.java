package cart.persistence.order;

import cart.application.repository.order.OrderRepository;
import cart.domain.order.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long createOrder(final Order order) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("member_id", order.getMember().getId());
        parameters.addValue("total_price", order.getTotalPrice());
        parameters.addValue("payment_price", order.getPaymentPrice());
        parameters.addValue("point", order.getPoint());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

}
