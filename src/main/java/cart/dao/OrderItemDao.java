package cart.dao;

import cart.domain.CartOrder;
import cart.domain.Member;
import cart.dto.OrderItemDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<OrderItemDto> findByCartOrderId(final Long cartOrderId) {
        final String sql = "SELECT order_item.id, order_item.product_id, order_item.name, order_item.price, order_item.image_url, order_item.quantity, cart_order.total_price, cart_order.created_at, member.id, member.email, member.cash " +
                "FROM order_item " +
                "INNER JOIN cart_order ON order_item.cart_order_id = cart_order.id " +
                "INNER JOIN member ON cart_order.member_id = member.id " +
                "WHERE order_item.cart_order_id = ?";
        return jdbcTemplate.query(sql, new Object[]{cartOrderId}, (rs, rowNum) -> {
            Long orderItemId = rs.getLong("order_item.id");
            Long productId = rs.getLong("order_item.product_id");
            String orderItemName = rs.getString("order_item.name");
            int orderItemPrice = rs.getInt("order_item.price");
            String orderItemUrl = rs.getString("order_item.image_url");
            int orderItemQuantity = rs.getInt("order_item.quantity");
            Long cartOrderTotalPrice = rs.getLong("cart_order.total_price");
            LocalDateTime cartOrderCreated = rs.getTimestamp("cart_order.created_at").toLocalDateTime();
            Long memberId = rs.getLong("member.id");
            String memberEmail = rs.getString("member.email");
            Long memberCash = rs.getLong("member.cash");
            Member member = Member.of(memberId, memberEmail, null, memberCash);
            CartOrder cartOrder = new CartOrder(cartOrderId, member, cartOrderTotalPrice, cartOrderCreated);
            return new OrderItemDto(orderItemId, cartOrder, productId, orderItemName, orderItemPrice, orderItemUrl, orderItemQuantity);
        });
    }

    public Long save(final OrderItemDto orderItem) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order_item (cart_order_id, product_id, name, price, image_url, quantity) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, orderItem.getCartOrder().getId());
            ps.setLong(2, orderItem.getProductId());
            ps.setString(3, orderItem.getName());
            ps.setInt(4, orderItem.getPrice());
            ps.setString(5, orderItem.getImageUrl());
            ps.setInt(6, orderItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
