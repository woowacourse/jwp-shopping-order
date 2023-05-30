package cart.dao;

import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.dto.OrderDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private static final RowMapper<OrderDto> ORDER_DTO_ROW_MAPPER = (rs, rowNum) -> new OrderDto(
        rs.getLong("order_id"),
        rs.getLong("product_id"),
        rs.getString("product_name"),
        rs.getInt("product_price"),
        rs.getString("product_image_url"),
        rs.getLong("order_item_id"),
        rs.getInt("product_quantity")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("shopping_order")
            .usingGeneratedKeyColumns("id");
    }

    public Order insert(final Order order) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("member_id", order.getMemberId());

        final long orderId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Order.persisted(orderId, order.getMember(), new OrderItems(order.getOrderItems()));
    }

    public List<OrderDto> findByOrderId(final Long orderId) {
        final String sql = "SELECT "
            + "ORD.id AS order_id, IT.id AS order_item_id, IT.quantity AS product_quantity, "
            + "PR.id AS product_id, PR.name AS product_name, PR.price AS product_price, PR.image_url AS product_image_url "
            + "FROM shopping_order AS ORD "
            + "INNER JOIN order_item AS IT ON IT.order_id = ORD.id "
            + "INNER JOIN product AS PR ON PR.id = IT.product_id "
            + "WHERE ORD.id = ?";
        System.out.println("sql = " + sql);
        return jdbcTemplate.query(sql, ORDER_DTO_ROW_MAPPER, orderId);
    }

    public List<OrderDto> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT "
            + "ORD.id AS order_id, IT.id AS order_item_id, IT.quantity AS product_quantity, "
            + "PR.id AS product_id, PR.name AS product_name, PR.price AS product_price, PR.image_url AS product_image_url "
            + "FROM shopping_order AS ORD "
            + "INNER JOIN order_item AS IT ON IT.order_id = ORD.id "
            + "INNER JOIN product AS PR ON PR.id = IT.product_id "
            + "WHERE ORD.member_id = ?";
        return jdbcTemplate.query(sql, ORDER_DTO_ROW_MAPPER, memberId);
    }
}
