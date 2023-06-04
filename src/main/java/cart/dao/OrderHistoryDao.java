package cart.dao;

import cart.domain.Member;
import cart.domain.OrderHistory;
import cart.domain.OrderItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderHistoryDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(OrderHistory orderHistory) {
        String sql = "INSERT INTO order_history (member_id, original_price, used_point, order_price) " +
                "VALUES (:memberId, :originalPrice, :usedPoint, :orderPrice)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("memberId", orderHistory.getMember().getId());
        params.addValue("originalPrice", orderHistory.getOriginalPrice());
        params.addValue("usedPoint", orderHistory.getUsedPoint());
        params.addValue("orderPrice", orderHistory.getOrderPrice());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<OrderHistory> findAllByMemberId(Long memberId) {
        String sql = "SELECT order_history.id as order_history_id, order_history.member_id, order_history.original_price, order_history.used_point, order_history.order_price, "
                + "order_item.id as order_item_id, order_item.product_id, order_item.name, order_item.price, order_item.image_url, order_item.quantity, "
                + "member.email, member.password, member.point "
                + "FROM order_history "
                + "INNER JOIN order_item ON order_history.id = order_item.order_history_id "
                + "INNER JOIN member ON order_history.member_id = member.id "
                + "WHERE member_id = :memberId";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("memberId", memberId);

        return jdbcTemplate.query(sql, parameters, (rs) -> {
            Map<Long, OrderHistory> orderHistoryMap = new HashMap<>();
            while (rs.next()) {
                Long orderHistoryId = rs.getLong("order_history_id");
                OrderHistory orderHistory;
                if (!orderHistoryMap.containsKey(orderHistoryId)) {
                    int originalPrice = rs.getInt("original_price");
                    int usedPoint = rs.getInt("used_point");
                    int orderPrice = rs.getInt("order_price");

                    Long findMemberId = rs.getLong("member_id");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    int point = rs.getInt("point");
                    Member member = new Member(findMemberId, email, password, point);

                    orderHistory = new OrderHistory(orderHistoryId, originalPrice, usedPoint, orderPrice, member);
                    orderHistoryMap.put(orderHistoryId, orderHistory);
                } else {
                    orderHistory = orderHistoryMap.get(orderHistoryId);
                }

                Long orderItemId = rs.getLong("order_item_id");
                Long productId = rs.getLong("product_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                String imageUrl = rs.getString("image_url");
                int quantity = rs.getInt("quantity");

                OrderItem orderItem = new OrderItem(orderItemId, orderHistory, productId, name, price, imageUrl, quantity);
                orderHistory.addOrderItem(orderItem);
            }
            return new ArrayList<>(orderHistoryMap.values());
        });
    }
}
