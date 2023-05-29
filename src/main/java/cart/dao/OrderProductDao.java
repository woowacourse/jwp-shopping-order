package cart.dao;

import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.domain.product.Product;
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
        this.jdbcTemplate = jdbcTemplate;;
    }

    final RowMapper<OrderProduct> orderProductRowMapper = (result, rowNum) -> {
        final Member member = new Member(
                result.getLong("member.id"),
                result.getString("email"),
                null);
        final Order order = new Order(
                result.getLong("orders.id"),
                member,
                new MemberPoint(result.getInt("used_point")),
                result.getTimestamp("orders.created_at").toLocalDateTime());
        final Product product = new Product(
                result.getLong("product.id"),
                result.getString("name"),
                result.getInt("price"),
                result.getString("image_url"));

        return new OrderProduct(
                result.getLong("order_product.id"),
                order, product,
                product.getProductName(),
                product.getProductPrice(),
                product.getProductImageUrl(),
                new Quantity(result.getInt("order_product.quantity")));
    };

    public void save(final List<OrderProduct> orderProducts) {
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

    public List<OrderProduct> findByOrderId(final Long orderId) {
        final String sql = "SELECT op.id, op.quantity, " +
                "m.id, m.email, " +
                "o.id, o.used_point, o.created_at, " +
                "p.id, p.name, p.price, p.image_url, " +
                "FROM order_product op " +
                "JOIN orders o ON o.id = op.order_id " +
                "JOIN product p ON p.id = op.product_id " +
                "JOIN member m ON m.id = o.member_id " +
                "WHERE op.order_id = ?";

        return jdbcTemplate.query(sql, orderProductRowMapper, orderId);
    }
}
