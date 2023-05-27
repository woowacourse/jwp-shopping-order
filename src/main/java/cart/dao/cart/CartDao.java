package cart.dao.cart;

import cart.domain.cart.CartItem;
import cart.entity.CartEntity;
import cart.entity.CartItemEntity;
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

    public CartDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

    private Long findCartId(final Long memberId) {
        String sqlForCartId = "SELECT id FROM cart WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sqlForCartId, Long.class, memberId);
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

    public void updateQuantity(final long cartItemId, final int quantity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, cartItemId);
    }

    public void removeCartItem(final Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isExistCartItem(final long cartItemId) {
        String sql = "SELECT COUNT(*) FROM cart_item WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cartItemId);

        return count != null && count > 0;
    }
}

