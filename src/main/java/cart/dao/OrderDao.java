package cart.dao;

import cart.domain.*;
import cart.dto.OrderDto;
import cart.exception.OrderNotFoundException;
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


        String sql = "insert into order_item(orders_id, member_id, product_name, price, image_url, quantity) values (?,?,?,?,?,?)";
        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        List<Object[]> orderItemParams = new ArrayList<>();
        for (CartItem cartItem : order.getCartItems()) {
            Object[] params = {
                    orderId,
                    cartItem.getMember().getId(),
                    cartItem.getProduct().getName(),
                    cartItem.getProduct().getPrice(),
                    cartItem.getProduct().getImageUrl(),
                    cartItem.getQuantity()
            };
            orderItemParams.add(params);
        }

        jdbcTemplate.batchUpdate(sql, orderItemParams);
        return orderId;
    }

    public OrderDto findById(Long orderId) throws OrderNotFoundException {
        String sql = "SELECT order_item.orders_id, order_item.member_id, member.email, order_item.product_name, " +
                "order_item.price, order_item.image_url, order_item.quantity " +
                "FROM order_item " +
                "INNER JOIN member ON order_item.member_id = member.id " +
                "WHERE order_item.orders_id = ?";

        String queryOrder = "select orders.price from orders where id = ?";

        List<CartItem> cartItems = new ArrayList<>();
        Integer price = 0;
        try {
             cartItems = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Long memberId = rs.getLong("member_id");
                String email = rs.getString("email");
                String productName = rs.getString("product_name");
                int itemPrice = rs.getInt("price");
                String imageUrl = rs.getString("image_url");
                int quantity = rs.getInt("quantity");
                Member member = new Member(memberId, email, null);
                Product product = new Product(null, productName, itemPrice, imageUrl);
                return new CartItem(null, quantity, product, member);
            }, orderId);

            price = jdbcTemplate.queryForObject(queryOrder, Integer.class, orderId);
        } catch (Exception e) {
            throw new OrderNotFoundException("해당 주문 id를 찾을 수 없습니다.");
        }

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
