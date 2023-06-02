package cart.dao;

import cart.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connect -> {
            PreparedStatement ps = connect.prepareStatement(
                    "INSERT INTO orders(member_id, product_price, discount_price, delivery_fee, total_price, created_at) " +
                            "VALUES(?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, order.getProductPrice());
            ps.setLong(3, order.getDiscountPrice());
            ps.setLong(4, order.getDeliveryFee());
            ps.setLong(5, order.getTotalPrice());
            ps.setDate(6, order.getDate());

            return ps;
        }, keyHolder);

        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        String relatedSql = "INSERT INTO order_items (order_id, product_name, product_price, product_image_url, product_quantity) " +
                "VALUES(?, ?, ?, ?, ?)";

        for (OrderItem orderItem : order.getOrderItems().getOrderItems()) {
            jdbcTemplate.update(relatedSql, orderId, orderItem.getProduct().getName(), orderItem.getProduct().getPrice(), orderItem.getProduct().getImageUrl(), orderItem.getQuantity());
        }

        return orderId;
    }

    public Order findById(Long id) {
        String sql = "SELECT orders.id, orders.member_id, member.email, " +
                "orders.product_price, orders.discount_price, orders.delivery_fee, orders.total_price, orders.created_at, " +
                "order_items.id, order_items.product_name, order_items.product_price, order_items.product_image_url, order_items.product_quantity " +
                "FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "INNER JOIN order_items ON orders.id = order_items.order_id " +
                "WHERE orders.id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper());
    }

    private RowMapper<Order> rowMapper() {
        return (rs, rowNum) -> {
            Long orderId = rs.getLong("orders.id");
            Long memberId = rs.getLong("orders.member_id");
            String email = rs.getString("member.email");

            Long productTotalPrice = rs.getLong("orders.product_price");
            Long discountPrice = rs.getLong("orders.discount_price");
            Long deliveryFee = rs.getLong("orders.delivery_fee");
            Long totalPrice = rs.getLong("orders.total_price");
            Date createdAt = rs.getDate("orders.created_at");

            List<OrderItem> orderItem = new ArrayList<>();
            Member member = new Member(memberId, email, null);

            do {
                Long orderItemsId = rs.getLong("order_items.id");
                String productName = rs.getString("order_items.product_name");
                int productPrice = rs.getInt("order_items.product_price");
                String productUrl = rs.getString("order_items.product_image_url");
                int productQuantity = rs.getInt("order_items.product_quantity");

                orderItem.add(new OrderItem(orderItemsId, new Product(productName, productPrice, productUrl), productQuantity));
            } while (rs.next());

            OrderItems orderItems = new OrderItems(orderItem);

            return new Order(orderId, member, orderItems, productTotalPrice, discountPrice, deliveryFee, totalPrice, createdAt);
        };
    }

    public List<Order> findAllByMember(Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";

        List<Long> orderId = jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> rs.getLong("id"));
        List<Order> orders = orderId.stream()
                .map(this::findById)
                .collect(Collectors.toList());

        return orders;
    }

    public void deleteById(Long id) {
        String sql = "UPDATE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


}
