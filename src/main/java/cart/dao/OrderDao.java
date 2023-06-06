package cart.dao;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Order order) {
        final long orderId = saveOrderInfo(order);
        saveItems(order, orderId);

        return orderId;
    }

    private long saveOrderInfo(Order order) {
        final MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("memberId", order.getOrderingMember().getId());
        sqlParameterSource.addValue("priceAfterDiscount", order.getPriceAfterDiscount());
        sqlParameterSource.addValue("orderDate", new Timestamp(System.currentTimeMillis()));

        return (long) simpleJdbcInsert.executeAndReturnKey(sqlParameterSource);
    }

    private void saveItems(Order order, long orderId) {
        final List<OrderItem> orderItems = order.getOrderItems();
        jdbcTemplate.batchUpdate("insert into order_items(order_id, name, product_price, quantity, image_url) values(?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final OrderItem orderItem = orderItems.get(i);
                final String name = orderItem.getName();
                final int productPrice = orderItem.getProductPrice();
                final int quantity = orderItem.getQuantity();
                final String imageUrl = orderItem.getImageUrl();

                ps.setLong(1, orderId);
                ps.setString(2, name);
                ps.setInt(3, productPrice);
                ps.setInt(4, quantity);
                ps.setString(5, imageUrl);
            }

            @Override
            public int getBatchSize() {
                return orderItems.size();
            }
        });
    }

    public Optional<Order> findById(long orderId) {
        final List<OrderItem> orderItems = findItems(orderId);

        try {
            final Order order = findOrder(orderId, orderItems);

            return Optional.of(Objects.requireNonNull(order));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<OrderItem> findItems(long orderId) {
        String orderItemQuery = "select * from order_items where order_id = ?";

        return jdbcTemplate.query(orderItemQuery, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final int quantity = rs.getInt("quantity");
            final int price = rs.getInt("product_price");
            final String imageUrl = rs.getString("image_url");
            return new OrderItem(id, name, price, quantity, imageUrl);
        }, orderId);
    }

    private Order findOrder(long orderId, List<OrderItem> orderItems) {
        String orderQuery = "select o.order_date, o.price_after_discount, m.id, m.email from orders as o " +
                "inner join members as m on o.member_id = m.id " +
                "where o.id = ?";

        return jdbcTemplate.queryForObject(orderQuery, (rs, rowNum) -> {
            final long memberId = rs.getLong("id");
            final String email = rs.getString("email");
            final Member member = new Member(memberId, email, null);

            final Timestamp orderTime = rs.getTimestamp("order_date");
            final long priceAfterDiscount = rs.getLong("price_after_discount");

            return Order.of(orderId, orderItems, member, priceAfterDiscount, orderTime);
        }, orderId);
    }

    public List<Order> findAllByMember(Member member) {
        String ordersQuery = "select id from orders where member_id = ?";
        final List<Long> orderIds = jdbcTemplate.query(ordersQuery, (rs, rowNum) -> rs.getLong("id"), member.getId());

        return orderIds.stream()
                       .map(this::findById)
                       .map(Optional::get)
                       .collect(Collectors.toList());
    }
}
