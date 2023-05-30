package cart.dao;

import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (resultSet, rowNum) -> new OrderItem(
            resultSet.getLong("id"),
            resultSet.getLong("order_id"),
            resultSet.getString("name"),
            new Money(resultSet.getLong("price")),
            resultSet.getString("image_url"),
            resultSet.getInt("quantity")
    );

    private static final ResultSetExtractor<List<Order>> ORDER_RESULT_EXTRACTOR = resultSet -> {
        final Map<Long, List<OrderItem>> orderItemsByOrderId = generateOrderItemsByOrderId(resultSet);
        return orderItemsByOrderId.entrySet()
                .stream()
                .map(entry -> new Order(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    };

    private static Map<Long, List<OrderItem>> generateOrderItemsByOrderId(final ResultSet resultSet)
            throws SQLException {
        final Map<Long, List<OrderItem>> orderItemsByOrderId = new LinkedHashMap<>();
        while (resultSet.next()) {
            final OrderItem extracted = ORDER_ITEM_ROW_MAPPER.mapRow(resultSet, resultSet.getRow());
            final Long orderId = extracted.getOrderId();
            final List<OrderItem> orderItems = orderItemsByOrderId.getOrDefault(orderId, new ArrayList<>());
            orderItems.add(extracted);
            orderItemsByOrderId.put(orderId, orderItems);
        }
        return orderItemsByOrderId;
    }

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

    public Long save(final Long memberId, final Money deliveryFee) {
        final Number id = simpleJdbcInsertForOrders.executeAndReturnKey(Map.of(
                "member_id", memberId,
                "delivery_fee", deliveryFee.getValue()
        ));
        return id.longValue();
    }

    public void saveOrderItems(final List<OrderItem> orderItems) {
        simpleJdbcInsertForOrderItem.executeBatch(SqlParameterSourceUtils.createBatch(orderItems));
    }

    public List<Order> findByMemberId(final Long memberId) {
        String sql = "SELECT order_item.id, order_item.order_id, "
                + "order_item.name, order_item.price, order_item.image_url, order_item.quantity "
                + "FROM orders "
                + "INNER JOIN order_item ON orders.id = order_item.order_id "
                + "WHERE orders.member_id = ? "
                + "ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, ORDER_RESULT_EXTRACTOR, memberId);
    }
}
