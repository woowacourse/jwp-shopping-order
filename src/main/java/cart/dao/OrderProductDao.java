package cart.dao;

import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberEmail;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.order.SavedPoint;
import cart.domain.order.UsedPoint;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderProduct> orderProductRowMapper = (result, rowNum) -> {
        final Member member = new Member(
                result.getLong("member_id"),
                new MemberEmail(result.getString("email")),
                null);
        final Order order = new Order(
                result.getLong("order_id"),
                member,
                new UsedPoint(result.getInt("used_point")),
                new SavedPoint(result.getInt("saved_point")),
                new DeliveryFee(result.getInt("delivery_fee")),
                result.getTimestamp("created_at").toLocalDateTime());

        return new OrderProduct(
                result.getLong("orderproduct_id"),
                order,
                result.getLong("product_id"),
                new ProductName(result.getString("product_name")),
                new ProductPrice(result.getInt("product_price")),
                new ProductImageUrl(result.getString("product_image_url")),
                new Quantity(result.getInt("quantity")));
    };

    public void insertAll(final List<OrderProduct> orderProducts) {
        final String sql = "INSERT INTO order_product(order_id, product_id, product_name, product_price, product_image_url, quantity) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                final OrderProduct orderProduct = orderProducts.get(i);
                ps.setLong(1, orderProduct.getOrderId());
                ps.setLong(2, orderProduct.getProductId());
                ps.setString(3, orderProduct.getProductNameValue());
                ps.setInt(4, orderProduct.getProductPriceValue());
                ps.setString(5, orderProduct.getProductImageUrlValue());
                ps.setInt(6, orderProduct.getQuantityValue());
            }

            @Override
            public int getBatchSize() {
                return orderProducts.size();
            }
        });
    }

    public List<OrderProduct> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT op.id AS orderproduct_id, op.product_id AS product_id, " +
                "op.product_name, op.product_price, op.product_image_url, op.quantity, " +
                "m.id AS member_id, m.email, " +
                "o.id AS order_id, o.used_point, o.saved_point, o.delivery_fee, o.created_at " +
                "FROM order_product op " +
                "JOIN orders o ON o.id = op.order_id " +
                "JOIN member m ON m.id = o.member_id " +
                "WHERE m.id = ?";

        return jdbcTemplate.query(sql, orderProductRowMapper, memberId);
    }

    public List<OrderProduct> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT op.id AS orderproduct_id, op.product_id AS product_id, " +
                "op.product_name, op.product_price, op.product_image_url, op.quantity, " +
                "m.id AS member_id, m.email, " +
                "o.id AS order_id, o.used_point, o.saved_point, o.delivery_fee, o.created_at " +
                "FROM order_product op " +
                "JOIN orders o ON o.id = op.order_id " +
                "JOIN member m ON m.id = o.member_id " +
                "WHERE o.id = ?";

        return jdbcTemplate.query(sql, orderProductRowMapper, orderId);
    }

    public void deleteAllByOrderId(final Long orderId) {
        final String sql = "DELETE FROM order_product WHERE order_id = ?";

        jdbcTemplate.update(sql, orderId);
    }
}
