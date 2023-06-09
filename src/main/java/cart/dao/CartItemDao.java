package cart.dao;

import cart.entity.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CartItemDao {
    private static final RowMapper<CartItemEntity> CART_ITEM_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new CartItemEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getLong("product_id"),
            resultSet.getInt("quantity"));
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertCartItem;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertCartItem = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM cart_item WHERE member_id = :member_id";
        final Map<String, Long> parameter = Map.of("member_id", memberId);
        return namedParameterJdbcTemplate.query(sql, parameter, CART_ITEM_ENTITY_ROW_MAPPER);
    }

    public Long save(CartItemEntity cartItemEntity) {
        final BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(cartItemEntity);
        return insertCartItem.executeAndReturnKey(parameters).longValue();
    }

    public CartItemEntity findById(Long id) {
        String sql = "SELECT * FROM cart_item WHERE id = :id";
        final Map<String, Long> parameter = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameter, CART_ITEM_ENTITY_ROW_MAPPER);
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = :member_id AND product_id = :product_id";
        final Map<String, Long> parameters = Map.of(
                "member_id", memberId,
                "product_id", productId
        );

        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, Map.of("id", id));
    }

    public void updateQuantity(CartItemEntity cartItem) {
        String sql = "UPDATE cart_item SET quantity = :quantity WHERE id = :id";

        final Map<String, ? extends Number> parameters = Map.of(
                "quantity", cartItem.getQuantity(),
                "id", cartItem.getId()
        );

        namedParameterJdbcTemplate.update(sql, parameters);
    }
}

