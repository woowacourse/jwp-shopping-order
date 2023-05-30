package cart.dao;

import cart.domain.Money;
import cart.domain.OrderItem;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsertForOrders;
    private final SimpleJdbcInsert simpleJdbcInsertForOrderItem;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsertForOrders = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "delivery_fee");
        this.simpleJdbcInsertForOrderItem = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("order_id", "name", "price", "image_url", "quantity");
    }

    public Long createOrder(final Long memberId, final Money deliveryFee) {
        Number id = simpleJdbcInsertForOrders.executeAndReturnKey(Map.of(
                "member_id", memberId,
                "delivery_fee", deliveryFee.getValue()
        ));
        return id.longValue();
    }

    public void addOrderItems(final List<OrderItem> orderItems) {
        simpleJdbcInsertForOrderItem.executeBatch(SqlParameterSourceUtils.createBatch(orderItems));
    }
}
