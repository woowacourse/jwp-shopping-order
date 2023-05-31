package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {

    private static final RowMapper<OrderEntity> orderRowMapper = (rs, rowNum) ->
            new OrderEntity(rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("used_points"),
                    rs.getInt("saving_rate"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert orderInsert;
    private final SimpleJdbcInsert orderItemInsert;


    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
        this.orderItemInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
    }


    public Long createOrder(final int usedPoints, final List<CartItem> cartItems, final int savingRate, final Member member) {

        final long id = orderInsert.executeAndReturnKey(Map.of("member_id", member.getId(), "used_points", usedPoints, "saving_rate", savingRate)).longValue();

        List<Map<String, Object>> batchValues = new ArrayList<>();
        for (final CartItem cartItem : cartItems) {
            Map<String, Object> batchMap = new HashMap<>();
            batchMap.put("order_id", id);
            batchMap.put("product_id", cartItem.getProduct().getId());
            batchMap.put("product_name", cartItem.getProduct().getName());
            batchMap.put("product_price", cartItem.getProduct().getPrice());
            batchMap.put("product_quantity", cartItem.getQuantity());
            batchMap.put("product_image_url", cartItem.getProduct().getImageUrl());
            batchValues.add(batchMap);
        }
        orderItemInsert.executeBatch(batchValues.toArray(new Map[0]));

        return id;
    }

    public OrderEntity findById(final Long id, final Long memberId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND member_id = ?";
        return jdbcTemplate.queryForObject(sql, orderRowMapper, id, memberId);
    }

    public List<OrderEntity> findAll(final Long memberId) {
        String sql = "SELECT * FROM orders WHERE member_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, memberId);
    }
}
