package cart.repository.dao;

import cart.domain.carts.CartItem;
import cart.repository.entity.CartItemEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<CartItemEntity> cartItemEntityRowMapper = ((rs, rowNum) ->
            new CartItemEntity.Builder()
                    .id(rs.getLong("id"))
                    .memberId(rs.getLong("member_id"))
                    .productId(rs.getLong("product_id"))
                    .quantity(rs.getInt("quantity"))
                    .build()
    );

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, cartItemEntityRowMapper, memberId);
    }

    public long save(CartItemEntity cartItemEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", cartItemEntity.getId());
        params.put("member_id", cartItemEntity.getMemberId());
        params.put("product_id", cartItemEntity.getProductId());
        params.put("quantity", cartItemEntity.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public long save(CartItem cartItem) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", cartItem.getId());
        params.put("member_id", cartItem.getMember().getId());
        params.put("product_id", cartItem.getProduct().getId());
        params.put("quantity", cartItem.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Optional<CartItemEntity> findById(long cartItemId) {
        try {
            String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, cartItemEntityRowMapper, cartItemId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

