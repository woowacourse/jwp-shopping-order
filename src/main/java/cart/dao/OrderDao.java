package cart.dao;

import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    
    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (member_id, product_price, discount_price, delivery_fee, total_price, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, order.getMemberId());
            ps.setInt(2, order.getProductPrice());
            ps.setInt(3, order.getDiscountPrice());
            ps.setInt(4, order.getDeliveryFee());
            ps.setInt(5, order.getTotalPrice());
            ps.setObject(6, order.getCreatedAt(), Types.TIMESTAMP);
            //todo : 자바 객체 뭐로 해야하는지 고민하기
            return ps;
        }, keyHolder);
        
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    
    public List<Order> findById(Long id) {
        String sql = "SELECT orders.id, orders.member_id, orders.delivery_fee, orders.discount_price, orders.created_at, "
                + "order_items.id, order_items.product_name, order_items.product_price, order_items.product_image_url, order_items.product_quantity "
                + "FROM orders "
                + "INNER JOIN order_items ON orders.id = order_items.order_id "
                + "WHERE orders.member_id = ? ";
        Map<Long, Order> orderMap = new HashMap<>();
        jdbcTemplate.query(sql, new OrderMapper(orderMap), id);
        return new ArrayList<>(orderMap.values());
    }
    
    
    private class OrderMapper implements RowMapper<Void> {
    
        private final Map<Long, Order> orderMap;
    
        public OrderMapper(Map<Long, Order> orderMap) {
            this.orderMap = orderMap;
        }
    
        @Override
        public Void mapRow(ResultSet rs, int rowNum) throws SQLException {//주문
            Long orderId = rs.getLong("orders.id");
            Long memberId = rs.getLong("member_id");
            Integer deliveryFee = rs.getInt("delivery_fee");
            Integer discountPrice = rs.getInt("discount_price");
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
    
            //주문 상품 정보
            Long orderItemId = rs.getLong("order_items.id");
            String productName = rs.getString("product_name");
            Integer productPrice = rs.getInt("product_price");
            String productImageUrl = rs.getString("product_price");
            Integer productQuantity = rs.getInt("product_quantity");
    
            OrderItem orderItem = new OrderItem(orderItemId, new Product(productName, productPrice, productImageUrl), productQuantity);
            
            if(orderMap.containsKey(orderId)) {
                Order order = orderMap.get(orderId);
                List<OrderItem> updatedOrderItems = order.getOrderItems();
                updatedOrderItems.add(orderItem);
                orderMap.replace(orderId, new Order(order.getId(), order.getMemberId(), updatedOrderItems,
                        order.getDeliveryFee(), order.getDiscountPrice(), order.getCreatedAt()));
                return null;
            }
            orderMap.put(orderId, new Order(orderId, memberId, List.of(orderItem), deliveryFee, discountPrice, createdAt));
            return null;
        }
    }
}
