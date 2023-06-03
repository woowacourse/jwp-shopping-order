package cart.dao.cart;

import cart.domain.cart.CartItem;
import cart.entity.cart.CartEntity;
import cart.entity.cart.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CartDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<CartEntity> cartRowMapper = (rs, rowNum) ->
            new CartEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id")
            );

    private final RowMapper<CartItemEntity> cartItemRowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getLong("cart_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity")
            );

    public List<CartItemEntity> findAllCartItemEntitiesByCartId(final Long cartId) {
        String sql = "SELECT * FROM cart_item WHERE cart_id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, cartId);
    }

    public CartEntity findCartEntityByMemberId(final long memberId) {
        String sql = "SELECT id, member_id FROM cart WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, cartRowMapper, memberId);
    }

    public Optional<CartItemEntity> findCartItemEntityById(final Long cartItemId) {
        String sql = "SELECT id, cart_id, product_id, quantity FROM cart_item WHERE id = :id";
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("id", cartItemId), cartItemRowMapper).stream()
                .findAny();
    }

    public Long saveCartItem(final long cartId, final CartItem cartItem) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cart_id", cartId);
        parameters.put("product_id", cartItem.getProduct().getId());
        parameters.put("quantity", cartItem.getQuantity());

        return insert.executeAndReturnKey(parameters).longValue();
    }

    public Long saveCartItem(final long cartId, final Long productId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cart_id", cartId);
        parameters.put("product_id", productId);
        parameters.put("quantity", 1);

        return insert.executeAndReturnKey(parameters).longValue();
    }

    public void updateQuantity(final long cartItemId, final int quantity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, cartItemId);
    }

    public void removeCartItemByCartITemId(final Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public long createMemberCart(final long memberId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("member_id", memberId);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }

    public void deleteAllCartItemsByCartId(final Long cartId) {
        String sql = "DELETE FROM cart_item WHERE cart_id = ?";
        jdbcTemplate.update(sql, cartId);
    }

    public boolean isExistAlreadyCartItem(final long cartId, final long productId) {
        String sql = "SELECT COUNT(*) FROM cart_item WHERE cart_id = ? AND product_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cartId, productId);
        return count > 0;
    }

    public CartItemEntity findCartItem(final Long cartId, final Long productId) {
        String sql = "SELECT * FROM cart_item WHERE cart_id = ? AND product_id = ?";
        return jdbcTemplate.queryForObject(sql, cartItemRowMapper, cartId, productId);
    }

    public boolean hasCartItem(final Long cartId, final Long cartItemId) {
        String sql = "SELECT COUNT(*) FROM cart_item WHERE cart_id = ? AND id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, cartId, cartItemId);
        return count > 0;
    }
}

