package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.*;

@Component
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order save(final Order order) {
        final Order persisted = savePrice(order);

        final List<CartItem> cartItems = persisted.getCartItems();
        final String sql = "INSERT INTO ordered_product(order_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, persisted.getId());
                ps.setLong(2, cartItems.get(i).getProduct().getId());
                ps.setInt(3, cartItems.get(i).getQuantity());
            }

            @Override
            public int getBatchSize() {
                return cartItems.size();
            }
        });

        return persisted;
    }

    private Order savePrice(final Order order) {
        final String sql = "INSERT INTO `order`(price, member_id) VALUES (?, ?)";

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getTotalPrice());
            ps.setLong(2, order.getMember().getId());

            return ps;
        }, keyHolder);

        final Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Order(key, order.getMember(), order.getTotalPrice(), order.getCartItems());
    }

    public Order findById(final Long orderId) {
        final String sql = "SELECT " +
                "o.id AS order_id, " +
                "o.price AS total_price, " +
                "m.id AS member_id, " +
                "m.email, " +
                "m.password, " +
                "op.quantity, " +
                "p.id AS product_id, " +
                "p.name, " +
                "p.price, " +
                "p.image_url " +
                "FROM `order` o " +
                "JOIN member m on o.member_id = m.id " +
                "JOIN ordered_product op on o.id = op.order_id " +
                "JOIN product p on op.product_id = p.id " +
                "WHERE o.id = ?;";

        final List<OrderEntity> orderEntities = jdbcTemplate.query(sql, (rs, rowNum) -> {
            final Member member = new Member(rs.getLong("member_id"), rs.getString("email"), rs.getString("password"));
            final Product product = new Product(rs.getLong("product_id"), rs.getString("name"), rs.getInt("price"), rs.getString("image_url"));
            final CartItem item = new CartItem(member, product);

            return new OrderEntity(rs.getLong("order_id"), rs.getInt("total_price"), item);
        }, orderId);

        if (orderEntities.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 주문 정보입니다.");
        }
        return toOrder(orderEntities);
    }

    public List<Order> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT " +
                "o.id AS order_id, " +
                "o.price AS total_price, " +
                "m.id AS member_id, " +
                "m.email, " +
                "m.password, " +
                "op.quantity, " +
                "p.id AS product_id, " +
                "p.name, " +
                "p.price, " +
                "p.image_url " +
                "FROM `order` o " +
                "JOIN member m on o.member_id = m.id " +
                "JOIN ordered_product op on o.id = op.order_id " +
                "JOIN product p on op.product_id = p.id " +
                "WHERE o.member_id = ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                    final Member member = new Member(rs.getLong("member_id"), rs.getString("email"), rs.getString("password"));
                    final Product product = new Product(rs.getLong("product_id"), rs.getString("name"), rs.getInt("price"), rs.getString("image_url"));
                    final CartItem item = new CartItem(member, product);

                    return new OrderEntity(rs.getLong("order_id"), rs.getInt("total_price"), item);
                }, memberId).stream()
                .collect(collectingAndThen(
                        groupingBy(orderEntity -> orderEntity.id),
                        ordersMap -> ordersMap.values().stream()
                                .map(this::toOrder)
                                .collect(toList())));
    }

    private Order toOrder(final List<OrderEntity> entities) {
        final Long id = entities.get(0).id;
        final Member member = entities.get(0).getMember();
        final int totalPrice = entities.get(0).totalPrice;
        final List<CartItem> cartItems = entities.stream()
                .map(orderEntity -> orderEntity.cartItem)
                .collect(toList());

        return new Order(id, member, totalPrice, cartItems);
    }

    private static class OrderEntity {
        private final Long id;
        private final int totalPrice;
        private final CartItem cartItem;

        private OrderEntity(final Long id, final int totalPrice, final CartItem cartItem) {
            this.id = id;
            this.totalPrice = totalPrice;
            this.cartItem = cartItem;
        }

        private Member getMember() {
            return cartItem.getMember();
        }
    }
}
