package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDao {

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

    public Long createOrder(final int usedPoints, final List<CartItem> cartItems, final Member member) {
        final var parameterSources = cartItems.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);

        final long id = orderInsert.executeAndReturnKey(Map.of("member_id", member.getId(), "used_points", usedPoints)).longValue();

        List<Map<String, Object>> batchValues = new ArrayList<>();
        for (final CartItem cartItem : cartItems) {
            Map<String, Object> batchMap = new HashMap<>();
            batchMap.put("order_id", id);
            batchMap.put("product_name", cartItem.getProduct().getName());
            batchMap.put("product_price", cartItem.getProduct().getPrice());
            batchMap.put("product_quantity", cartItem.getQuantity());
            batchMap.put("product_image_url", cartItem.getProduct().getImageUrl());
            batchValues.add(batchMap);
        }
        orderItemInsert.executeBatch(batchValues.toArray(new Map[0]));

        return id;
    }
}
