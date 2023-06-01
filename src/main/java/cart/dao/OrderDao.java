package cart.dao;

import cart.domain.*;
import cart.dto.OrderDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(Order order, Money discounting) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into orders (member_id, price, discounted) values (?,?,?)",
                    new String[]{"id"}
            );
            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, order.getPrice().toInt());
            ps.setInt(3, discounting.toInt());
            return ps;
        }, keyHolder);


        String sql = "insert into order_item(orders_id, cart_item_id) values (?,?)";
        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        List<Object[]> cartItemIds = new ArrayList<>();
        for (Long cartItemId : order.getCartItemIds()) {
            cartItemIds.add(new Object[]{orderId, cartItemId});
        }

        jdbcTemplate.batchUpdate(sql, cartItemIds);
        return orderId;
    }

    public OrderDto findById(Long orderId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM orders " +
                "INNER JOIN order_item ON orders.id = order_item.orders_id " +
                "INNER JOIN cart_item ON cart_item.id = order_item.cart_item_id " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE orders.id = ?";

        List<CartItem> cartItems = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        }, orderId);

        String queryOrder = "select orders.price from orders where id = ?";
        Integer price = jdbcTemplate.queryForObject(queryOrder, Integer.class, orderId);

        return new OrderDto(orderId, cartItems, price);
    }

    public List<OrderDto> findByMemberId(Long memberId) {
        String sql = "SELECT id FROM orders WHERE member_id = ?";

        List<Long> orderIds = jdbcTemplate.queryForList(sql, Long.class, memberId);

        List<OrderDto> orderDtos = new ArrayList<>();
        for (Long orderId : orderIds) {
            orderDtos.add(findById(orderId));
        }

        return orderDtos;
    }
}
