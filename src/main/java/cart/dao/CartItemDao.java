package cart.dao;

import cart.entity.CartItemEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity")
            );

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long save(final CartItemEntity cartItem) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", cartItem.getMemberId());
        parameters.put("product_id", cartItem.getProductId());
        parameters.put("quantity", cartItem.getQuantity());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT * FROM cart_item WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItemEntity cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

