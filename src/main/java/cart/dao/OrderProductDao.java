package cart.dao;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderProduct> findAll() {
        final String findAllQuery =
                "SELECT order_products.id, order_products.quantity, "
                        + "orders.id, orders.created_at, orders.total_price, orders.final_price, "
                        + "products.id, products.name, products.price, products.image_url, "
                        + "members.id, members.email, members.password "
                        + "FROM order_products "
                        + "INNER JOIN orders ON order_products.order_id = orders.id "
                        + "INNER JOIN products ON order_products.product_id = products.id "
                        + "INNER JOIN members ON orders.member_id = members.id";

        return jdbcTemplate.query(findAllQuery, orderProductRowMapper());
    }

    public void saveOrderProducts(final List<OrderProduct> orderProducts) {
        final String saveCarQuery = "INSERT INTO order_products(order_id, product_id, quantity) VALUES (?, ?, ?)";

        final List<Object[]> insertValues = new ArrayList<>();
        for (final OrderProduct orderProduct : orderProducts) {
            insertValues.add(new Object[]{
                    orderProduct.getOrder().getId(),
                    orderProduct.getProduct().getId(),
                    orderProduct.getQuantity()
            });
        }
        jdbcTemplate.batchUpdate(saveCarQuery, insertValues);
    }

    public List<OrderProduct> findByOrderId(final Long orderId) {
        final String findByOrderIdQuery =
                "SELECT order_products.id, order_products.quantity, "
                        + "orders.id, orders.created_at, orders.total_price, orders.final_price, "
                        + "products.id, products.name, products.price, products.image_url, "
                        + "members.id, members.email, members.password "
                        + "FROM order_products "
                        + "INNER JOIN orders ON order_products.order_id = orders.id "
                        + "INNER JOIN products ON order_products.product_id = products.id "
                        + "INNER JOIN members ON orders.member_id = members.id "
                        + "WHERE order_products.order_id = ?";

        return jdbcTemplate.query(findByOrderIdQuery, orderProductRowMapper(), orderId);
    }


    public List<OrderProduct> findByMemberId(final Long memberId) {
        final String findAllQuery =
                "SELECT order_products.id, order_products.quantity, "
                        + "orders.id, orders.created_at, orders.total_price, orders.final_price, "
                        + "products.id, products.name, products.price, products.image_url, "
                        + "members.id, members.email, members.password "
                        + "FROM order_products "
                        + "INNER JOIN orders ON order_products.order_id = orders.id "
                        + "INNER JOIN products ON order_products.product_id = products.id "
                        + "INNER JOIN members ON orders.member_id = members.id "
                        + "WHERE members.id = ?";

        return jdbcTemplate.query(findAllQuery, orderProductRowMapper(), memberId);
    }

    private RowMapper<OrderProduct> orderProductRowMapper() {
        return (rs, rowNum) -> {
            final Member member = memberMapper(rs);
            final Order order = orderMapper(rs, member);
            final Product product = productMapper(rs);

            final long orderProductId = rs.getLong("id");
            final int quantity = rs.getInt("quantity");
            return new OrderProduct(orderProductId, order, product, quantity);
        };
    }

    private Member memberMapper(final ResultSet rs) throws SQLException {
        final Long memberId = rs.getLong("members.id");
        final String email = rs.getString("email");
        final String password = rs.getString("password");
        return new Member(memberId, email, password);
    }

    private Order orderMapper(final ResultSet rs, final Member member) throws SQLException {
        final long orderId = rs.getLong("orders.id");
        final LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        final int totalPrice = rs.getInt("total_price");
        final int finalPrice = rs.getInt("final_price");

        return new Order(orderId, createdAt, member, totalPrice, finalPrice);
    }

    private Product productMapper(final ResultSet rs) throws SQLException {
        final Long productId = rs.getLong("products.id");
        final String name = rs.getString("name");
        final int price = rs.getInt("price");
        final String imageUrl = rs.getString("image_url");
        return new Product(productId, name, price, imageUrl);
    }
}
