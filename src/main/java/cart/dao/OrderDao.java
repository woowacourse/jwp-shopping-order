package cart.dao;

import cart.domain.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderDetail> getOrderDetailsByOrdersId(Long ordersId) {
        String sql = "SELECT order_detail.id, orders.id, orders.member_id, member.email, orders.point_id, point.earned_point," +
                " point.left_point, point.expired_at, point.created_at, orders.used_point, orders.created_at, order_detail.product_id," +
                " order_detail.product_name, order_detail.product_price, order_detail.product_image_url, order_detail.order_quantity " +
                "FROM order_detail " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "INNER JOIN point ON orders.point_id = point.id " +
                "INNER JOIN orders ON orders.id = order_detail.orders_id " +
                "WHERE orders.id = ?";
        return jdbcTemplate.query(sql, new Object[]{ordersId}, (rs, rowNum) -> {
            Long id = rs.getLong("order_detail.id");
            Long orderId = rs.getLong("orders.id");
            Long pointId = rs.getLong("point_id");
            int earnedPoint = rs.getInt("earned_point");
            int leftPoint = rs.getInt("left_point");
            LocalDateTime pointCreatedAt = rs.getTimestamp("point.created_at").toLocalDateTime();
            LocalDateTime expiredAt = rs.getTimestamp("expired_at").toLocalDateTime();
            int usedPoint = rs.getInt("used_point");
            LocalDateTime ordersCreatedAt = rs.getTimestamp("orders.created_at").toLocalDateTime();
            Long productId = rs.getLong("product_id");
            String productName = rs.getString("product_name");
            int productPrice = rs.getInt("product_price");
            String productImageUrl = rs.getString("product_image_url");
            int orderQuantity = rs.getInt("order_quantity");
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");

            Member member = new Member(memberId, email, null);
            Point point = new Point(pointId, earnedPoint, leftPoint, member, expiredAt, pointCreatedAt);
            Orders orders = new Orders(orderId, member, point, earnedPoint, usedPoint, ordersCreatedAt);
            Product product = new Product(productId, productName, productPrice, productImageUrl, 0);

            return new OrderDetail(id, orders, product, productName, productPrice, productImageUrl, orderQuantity);
        });
    }

    public List<Orders> getOrdersByMemberId(Long memberId) {
        String sql = "SELECT orders.id, orders.member_id, member.email, orders.point_id, point.earned_point, point.left_point, point.expired_at, point.created_at, orders.used_point, orders.created_at " +
                "FROM orders " +
                "INNER JOIN member ON orders.member_id = member.id " +
                "INNER JOIN point ON orders.point_id = point.id " +
                "WHERE orders.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Long id = rs.getLong("orders.id");
            Long pointId = rs.getLong("point_id");
            int earnedPoint = rs.getInt("earned_point");
            int leftPoint = rs.getInt("left_point");
            LocalDateTime pointCreatedAt = rs.getTimestamp("point.created_at").toLocalDateTime();
            LocalDateTime expiredAt = rs.getTimestamp("expired_at").toLocalDateTime();
            int usedPoint = rs.getInt("used_point");
            LocalDateTime ordersCreatedAt = rs.getTimestamp("orders.created_at").toLocalDateTime();
            String email = rs.getString("email");

            Member member = new Member(memberId, email, null);
            Point point = new Point(pointId, earnedPoint, leftPoint, member, expiredAt, pointCreatedAt);

            return new Orders(id, member, point, earnedPoint, usedPoint, ordersCreatedAt);
        });
    }

    public Long createOrders(Orders orders) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (member_id, point_id, earned_point, used_point, created_at) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orders.getMember().getId());
            ps.setLong(2, orders.getPoint().getId());
            ps.setInt(3, orders.getEarnedPoint());
            ps.setInt(4, orders.getUsedPoint());
            ps.setTimestamp(5, Timestamp.valueOf(orders.getCreatedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long createOrderDetail(OrderDetail orderDetail) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order_detail (orders_id, product_id, product_name, product_price, product_image_url, order_quantity) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderDetail.getOrders().getId());
            ps.setLong(2, orderDetail.getProduct().getId());
            ps.setString(3, orderDetail.getProductName());
            ps.setInt(4, orderDetail.getProductPrice());
            ps.setString(5, orderDetail.getProductImageUrl());
            ps.setInt(6, orderDetail.getOrderQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Orders> getOrdersByOrderId(Long id) {
        try {
            String sql = "SELECT orders.id, orders.member_id, member.email, orders.point_id, point.earned_point, point.left_point, point.expired_at, point.created_at, orders.used_point, orders.created_at " +
                    "FROM orders " +
                    "INNER JOIN member ON orders.member_id = member.id " +
                    "INNER JOIN point ON orders.point_id = point.id " +
                    "WHERE orders.id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Long pointId = rs.getLong("point_id");
                int earnedPoint = rs.getInt("earned_point");
                int leftPoint = rs.getInt("left_point");
                LocalDateTime pointCreatedAt = rs.getTimestamp("point.created_at").toLocalDateTime();
                LocalDateTime expiredAt = rs.getTimestamp("expired_at").toLocalDateTime();
                int usedPoint = rs.getInt("used_point");
                LocalDateTime ordersCreatedAt = rs.getTimestamp("point.created_at").toLocalDateTime();
                Long memberId = rs.getLong("member_id");
                String email = rs.getString("email");

                Member member = new Member(memberId, email, null);
                Point point = new Point(pointId, earnedPoint, leftPoint, member, expiredAt, pointCreatedAt);

                return new Orders(id, member, point, earnedPoint, usedPoint, ordersCreatedAt);
            }));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
