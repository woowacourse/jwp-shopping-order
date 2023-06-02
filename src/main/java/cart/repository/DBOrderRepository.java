package cart.repository;

import cart.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    private static Coupon getCoupon(ResultSet rs) throws SQLException {
        if (rs.getLong("coupon_id") == 0) {
            return null;
        }
        Long couponId = rs.getLong("coupon_id");
        String couponName = rs.getString("coupon_name");
        int discountValue = rs.getInt("discount_value");
        int minimumOrderAmount = rs.getInt("minimum_order_amount");
        LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
        return new Coupon(couponId, couponName, discountValue, minimumOrderAmount, endDate);
    }

    private static Member getMember(ResultSet rs) throws SQLException {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        return new Member(memberId, email, password);
    }

    public DBOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> findByMemberId(Long memberId) {
        String sql = "SELECT " +
                "o.id AS order_id, " +
                "oi.id AS order_item_id, " +
                "oi.product_id, " +
                "oi.product_name, " +
                "oi.product_price, " +
                "oi.product_image_url, " +
                "oi.quantity, " +
                "m.id AS member_id, " +
                "m.email, " +
                "m.password, " +
                "c.id AS coupon_id, " +
                "c.name AS coupon_name, " +
                "c.discount_value, " +
                "c.minimum_order_amount, " +
                "c.end_date, " +
                "o.delivery_fee, " +
                "o.payed_price, " +
                "o.order_date " +
                "FROM `order` o " +
                "JOIN order_item oi ON o.id = oi.order_id " +
                "JOIN member m ON o.member_id = m.id " +
                "LEFT JOIN order_coupon oc ON o.id = oc.order_id " +
                "LEFT JOIN coupon c ON oc.coupon_id = c.id " +
                "WHERE member_id = ?";

        return jdbcTemplate.query(sql, rs -> {
            List<Order> orders = new ArrayList<>();
            Long orderId = null;
            Order currentOrder = null;

            while (rs.next()) {
                if (orderId == null || rs.getLong("order_id") != orderId) {
                    orderId = rs.getLong("order_id");
                    int deliveryFee = rs.getInt("delivery_fee");
                    int finalPrice = rs.getInt("payed_price");
                    LocalDateTime orderTime = rs.getTimestamp("order_date").toLocalDateTime();
                    Member member = getMember(rs);
                    Coupon coupon = getCoupon(rs);
                    List<OrderItem> orderItems = new ArrayList<>();

                    currentOrder = new Order(orderId, orderItems, member, coupon, deliveryFee, finalPrice, orderTime);
                    orders.add(currentOrder);
                }

                Long orderItemId = rs.getLong("order_item_id");
                Long productId = rs.getLong("product_id");
                String productName = rs.getString("product_name");
                int productPrice = rs.getInt("product_price");
                String productImageUrl = rs.getString("product_image_url");
                int quantity = rs.getInt("quantity");

                Product product = new Product(productId, productName, productPrice, productImageUrl);
                OrderItem orderItem = new OrderItem(orderItemId, product, quantity);

                if (currentOrder != null) {
                    currentOrder.getOrderItems().add(orderItem);
                }
            }

            return orders;
        }, memberId);
    }
}
