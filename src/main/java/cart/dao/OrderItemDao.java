package cart.dao;

import java.time.LocalDateTime;
import java.util.List;

import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.orderitem.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {

    private static final String JOIN_SQL = "SELECT cart_order.member_id, " +
            "member.email, member.password, member.cash, " +
            "cart_order.id, cart_order.total_price, cart_order.created_at, " +
            "order_item.id, order_item.product_id, order_item.name, order_item.price, order_item.image_url, order_item.quantity, " +
            "FROM order_item " +
            "INNER JOIN cart_order ON cart_order.id = order_item.cart_order_id " +
            "INNER JOIN member ON member.id = cart_order.member_id ";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrderItem> rowMapper = (rs, rowNum) -> {
        long memberId = rs.getLong("cart_order.member_id");
        String email = rs.getString("member.email");
        String password = rs.getString("member.password");
        int cash = rs.getInt("member.cash");
        Member member = new Member(memberId, email, password, cash);

        long orderId = rs.getLong("cart_order.id");
        int totalPrice = rs.getInt("cart_order.total_price");
        LocalDateTime createdAt = rs.getTimestamp("cart_order.created_at").toLocalDateTime();
        Order order = new Order(orderId, member, totalPrice, createdAt);

        long orderItemId = rs.getLong("order_item.id");
        long productId = rs.getLong("order_item.product_id");
        String name = rs.getString("order_item.name");
        int price = rs.getInt("order_item.price");
        String imageUrl = rs.getString("order_item.image_url");
        int quantity = rs.getInt("order_item.quantity");

        return new OrderItem(orderItemId, order, productId, name, price, imageUrl, quantity);
    };

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item").usingGeneratedKeyColumns("id");
    }

    public Long insert(OrderItem orderItem) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cart_order_id", orderItem.getOrder().getId())
                .addValue("product_id", orderItem.getProductId())
                .addValue("name", orderItem.getName())
                .addValue("price", orderItem.getPrice())
                .addValue("image_url", orderItem.getImageUrl())
                .addValue("quantity", orderItem.getQuantity());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<OrderItem> selectAllByMemberId(Long memberId) {
        String sql = JOIN_SQL +
                "WHERE cart_order.member_id = ? " +
                "ORDER BY order_item.cart_order_id DESC, order_item.product_id DESC";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<OrderItem> selectAllByMemberIdAndOrderId(Long memberId, Long orderId) {
        String sql = JOIN_SQL +
                "WHERE cart_order.member_id = ? AND order_item.cart_order_id = ? " +
                "ORDER BY order_item.product_id DESC";
        return jdbcTemplate.query(sql, rowMapper, memberId, orderId);
    }
}
