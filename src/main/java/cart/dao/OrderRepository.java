package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.Item;
import cart.domain.order.Order;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addOrder(final Long memberId, final Order order) {
        String sql = "INSERT INTO `order` (member_id, total_price, discounted_total_price, delivery_price, ordered_at) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, memberId);
            ps.setInt(2, order.getTotalPrice());
            ps.setInt(3, order.getDiscountedTotalPrice());
            ps.setInt(4, order.getDeliveryPrice());
            ps.setTimestamp(5, Timestamp.valueOf(order.getOrderedAt()));
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void addOrderProducts(final Long orderId, final List<Item> items) {
        String sql = "INSERT INTO order_product (order_id, product_id, ordered_product_price, quantity) VALUES (:orderId, :productId, :productPrice, :quantity)";
        SqlParameterSource[] batchParams = items.stream()
                .map(item -> new MapSqlParameterSource()
                        .addValue("orderId", orderId)
                        .addValue("productId", item.getProduct().getId())
                        .addValue("productPrice", item.getProduct().getPrice())
                        .addValue("quantity", item.getQuantity()))
                .toArray(SqlParameterSource[]::new);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.batchUpdate(sql, batchParams);
    }

    public void addOrderCoupon(final Long orderId, final Long couponId) {
        String sql = "INSERT INTO order_coupon (order_id, coupon_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, orderId, couponId);
    }

    public Order findOrderByIdAndMemberId(final Long memberId, final Long orderId) {
        String sql = "SELECT " +
                "`order`.id, `order`.total_price, `order`.discounted_total_price, `order`.delivery_price, `order`.ordered_at, " +
                "order_product.id, order_product.product_id, product.name, order_product.ordered_product_price, product.image_url, order_product.quantity, " +
                "coupon.* " +
                "FROM `order` " +
                "LEFT JOIN order_product ON `order`.id = order_product.order_id " +
                "LEFT JOIN order_coupon ON `order`.id = order_coupon.order_id " +
                "LEFT JOIN product ON order_product.product_id = product.id " +
                "LEFT JOIN coupon ON order_coupon.coupon_id = coupon.id " +
                "WHERE `order`.member_id = ? AND `order`.id = ?";

        return jdbcTemplate.query(sql, rs -> {
            List<Item> items = new ArrayList<>();
            Coupon coupon = null;
            int totalPrice = 0;
            int deliveryPrice = 0;
            LocalDateTime orderedAt = null;
            while (rs.next()) {
                Product product = new Product(
                        rs.getLong("order_product.product_id"),
                        rs.getString("product.name"),
                        rs.getInt("order_product.ordered_product_price"),
                        rs.getString("product.image_url")
                );
                items.add(new Item(rs.getLong("order_product.id"), product, rs.getInt("order_product.quantity")));

                Long couponId = rs.getObject("coupon.id", Long.class);
                if (couponId != null) {
                    coupon = new Coupon(
                            couponId,
                            rs.getString("coupon.name"),
                            rs.getInt("coupon.discount_rate"),
                            rs.getInt("coupon.period"),
                            rs.getTimestamp("coupon.expired_at").toLocalDateTime()
                    );
                }
                totalPrice = rs.getInt("order.total_price");
                deliveryPrice = rs.getInt("order.delivery_price");
                orderedAt = rs.getTimestamp("order.ordered_at").toLocalDateTime();
            }

            return new Order(orderId, items, coupon, totalPrice, deliveryPrice, orderedAt);
        }, memberId, orderId);
    }

    public List<Order> findOrdersByMemberId(final Long memberId) {
        String sql = "SELECT " +
                "`order`.id, `order`.total_price, `order`.discounted_total_price, `order`.delivery_price, `order`.ordered_at, " +
                "order_product.id, order_product.product_id, product.name, order_product.ordered_product_price, product.image_url, order_product.quantity, " +
                "coupon.* " +
                "FROM `order` " +
                "LEFT JOIN order_product ON `order`.id = order_product.order_id " +
                "LEFT JOIN order_coupon ON `order`.id = order_coupon.order_id " +
                "LEFT JOIN product ON order_product.product_id = product.id " +
                "LEFT JOIN coupon ON order_coupon.coupon_id = coupon.id " +
                "WHERE `order`.member_id = ? " +
                "ORDER BY `order`.id";

        List<Order> orders = new ArrayList<>();
        jdbcTemplate.query(sql, rs -> {
            Long orderId = null;
            int totalPrice = 0;
            int deliveryPrice = 0;
            LocalDateTime orderedAt = null;

            List<Item> items = new ArrayList<>();
            Coupon coupon = null;

            while (rs.next()) {
                if (orderId != null && rs.getLong("order.id") != orderId) {
                    orders.add(new Order(orderId, items, coupon, totalPrice, deliveryPrice, orderedAt));
                    coupon = null;
                    items = new ArrayList<>();
                }
                orderId = rs.getLong("order.id");
                totalPrice = rs.getInt("order.total_price");
                deliveryPrice = rs.getInt("order.delivery_price");
                orderedAt = rs.getTimestamp("order.ordered_at").toLocalDateTime();

                Product product = new Product(
                        rs.getLong("order_product.product_id"),
                        rs.getString("product.name"),
                        rs.getInt("order_product.ordered_product_price"),
                        rs.getString("product.image_url")
                );
                items.add(new Item(rs.getLong("order_product.id"), product, rs.getInt("order_product.quantity")));

                Long couponId = rs.getObject("coupon.id", Long.class);
                if (couponId != null) {
                    coupon = new Coupon(
                            couponId,
                            rs.getString("coupon.name"),
                            rs.getInt("coupon.discount_rate"),
                            rs.getInt("coupon.period"),
                            rs.getTimestamp("coupon.expired_at").toLocalDateTime()
                    );
                }
            }
            orders.add(new Order(orderId, items, coupon, totalPrice, deliveryPrice, orderedAt));
        }, memberId);
        return orders;
    }
}
