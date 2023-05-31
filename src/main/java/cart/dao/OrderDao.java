package cart.dao;

import cart.domain.order.Order;
import cart.domain.order.OrderItems;
import cart.domain.price.OrderPrice;
import cart.dto.OrderDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("orders")
            .usingGeneratedKeyColumns("id");
    }

    public Order insert(final Order order, final OrderPrice orderPrice) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("member_id", order.getMemberId());
        params.addValue("product_price", orderPrice.getProductPrice());
        params.addValue("discount_price", orderPrice.getDiscountPrice());
        params.addValue("delivery_fee", orderPrice.getDeliveryFee());
        params.addValue("total_price", orderPrice.getTotalPrice());
        params.addValue("created_at", order.getOrderTime());

        final long orderId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Order.persisted(orderId, order.getMember(),
            new OrderItems(order.getOrderItems()), order.getOrderTime());
    }

    public List<OrderDto> findByOrderId(final Long orderId) {
        final String sql = "SELECT "
            + "ORD.id AS order_id, "
            + "ORD.created_at AS order_time, "
            + "ORD.product_price AS order_product_price, "
            + "ORD.discount_price AS order_discount_price, "
            + "ORD.delivery_fee AS order_delivery_fee, "
            + "ORD.total_price AS order_total_price, "
            + "IT.id AS order_item_id, "
            + "IT.product_name AS order_item_name, "
            + "IT.product_price AS order_item_price, "
            + "IT.product_image_url AS order_item_image_url, "
            + "IT.product_quantity AS order_item_quantity "
            + "FROM orders AS ORD "
            + "INNER JOIN order_items AS IT ON IT.order_id = ORD.id "
            + "WHERE ORD.id = ?";

        return jdbcTemplate.query(sql, new OrderDtoRowMapper(), orderId);
    }

    public List<OrderDto> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT "
            + "ORD.id AS order_id, "
            + "ORD.created_at AS order_time, "
            + "ORD.product_price AS order_product_price, "
            + "ORD.discount_price AS order_discount_price, "
            + "ORD.delivery_fee AS order_delivery_fee, "
            + "ORD.total_price AS order_total_price, "
            + "IT.id AS order_item_id, "
            + "IT.product_name AS order_item_name, "
            + "IT.product_price AS order_item_price, "
            + "IT.product_image_url AS order_item_image_url, "
            + "IT.product_quantity AS order_item_quantity "
            + "FROM orders AS ORD "
            + "INNER JOIN order_items AS IT ON IT.order_id = ORD.id "
            + "WHERE ORD.member_id = ?";

        return jdbcTemplate.query(sql, new OrderDtoRowMapper(), memberId);
    }

    private static class OrderDtoRowMapper implements RowMapper<OrderDto> {

        @Override
        public OrderDto mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new OrderDto(
                rs.getLong("order_id"),
                rs.getTimestamp("order_time").toLocalDateTime(),
                rs.getLong("order_product_price"),
                rs.getLong("order_discount_price"),
                rs.getLong("order_delivery_fee"),
                rs.getLong("order_total_price"),
                rs.getLong("order_item_id"),
                rs.getString("order_item_name"),
                rs.getInt("order_item_price"),
                rs.getString("order_item_image_url"),
                rs.getInt("order_item_quantity")
            );
        }
    }
}
