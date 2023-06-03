package cart.repository;

import cart.domain.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DBOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public DBOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO `order` (member_id, delivery_fee, payed_price, order_date) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getMember().getId());
            ps.setInt(2, order.getDeliveryFee());
            ps.setInt(3, order.getFinalPrice());
            ps.setTimestamp(4, Timestamp.valueOf(order.getOrderTime()));
            return ps;
        }, keyHolder);

        Long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        saveOrderItems(orderId, order.getOrderItems());
        if (order.getCoupon() != null) {
            saveOrderCoupon(orderId, order.getCoupon().getId());
        }
        return orderId;
    }

    private void saveOrderCoupon(Long orderId, Long couponId) {
        String sql = "INSERT INTO order_coupon (order_id, coupon_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, orderId, couponId);
    }

    private void saveOrderItems(Long orderId, List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item (order_id, product_id, product_name, product_price, product_image_url, quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                OrderItem orderItem = orderItems.get(i);
                ps.setLong(1, orderId);
                ps.setLong(2, orderItem.getProduct().getId());
                ps.setString(3, orderItem.getProduct().getName());
                ps.setInt(4, orderItem.getProduct().getPrice());
                ps.setString(5, orderItem.getProduct().getImageUrl());
                ps.setInt(6, orderItem.getQuantity());
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
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
            boolean isOrderExist = false;
            List<Order> orders = new ArrayList<>();
            Long orderId = null;
            List<OrderItem> orderItems = new ArrayList<>();
            Member member = null;
            Coupon coupon = null;
            int deliveryFee = 0;
            int finalPrice = 0;
            LocalDateTime orderTime = null;

            while (rs.next()) {
                isOrderExist = true;
                if (orderId == null || rs.getLong("order_id") != orderId) {
                    if (orderId != null) {
                        orders.add(new Order(orderId, new OrderItems(orderItems), member, coupon, deliveryFee, finalPrice, orderTime));
                    }
                    orderId = rs.getLong("order_id");
                    orderItems = new ArrayList<>();
                    member = getMember(rs);
                    coupon = getCoupon(rs);
                    deliveryFee = rs.getInt("delivery_fee");
                    finalPrice = rs.getInt("payed_price");
                    orderTime = rs.getTimestamp("order_date").toLocalDateTime();
                }

                Long orderItemId = rs.getLong("order_item_id");
                Long productId = rs.getLong("product_id");
                String productName = rs.getString("product_name");
                int productPrice = rs.getInt("product_price");
                String productImageUrl = rs.getString("product_image_url");
                int quantity = rs.getInt("quantity");

                Product product = new Product(productId, productName, productPrice, productImageUrl);
                orderItems.add(new OrderItem(orderItemId, product, quantity));
            }

            if (isOrderExist) {
                orders.add(new Order(orderId, new OrderItems(orderItems), member, coupon, deliveryFee, finalPrice, orderTime));
            }

            return orders;
        }, memberId);
    }

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

    @Override
    public Order findById(Long orderId) {
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
                "WHERE o.id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            int deliveryFee = rs.getInt("delivery_fee");
            int finalPrice = rs.getInt("payed_price");
            LocalDateTime orderTime = rs.getTimestamp("order_date").toLocalDateTime();

            Member member = getMember(rs);
            Coupon coupon = getCoupon(rs);

            List<OrderItem> orderItems = new ArrayList<>();
            do {
                Long orderItemId = rs.getLong("order_item_id");
                Long productId = rs.getLong("product_id");
                String productName = rs.getString("product_name");
                int productPrice = rs.getInt("product_price");
                String productImageUrl = rs.getString("product_image_url");
                int quantity = rs.getInt("quantity");

                Product product = new Product(productId, productName, productPrice, productImageUrl);
                OrderItem orderItem = new OrderItem(orderItemId, product, quantity);

                orderItems.add(orderItem);
            } while (rs.next() && rs.getLong("order_id") == orderId);

            return new Order(orderId, new OrderItems(orderItems), member, coupon, deliveryFee, finalPrice, orderTime);
        }, orderId);
    }
}
