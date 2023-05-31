package cart.dao;

import cart.entity.CartItemEntity;
import org.springframework.dao.DataAccessException;
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
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getInt("quantity"),
                    rs.getLong("product_id"),
                    rs.getLong("member_id")
            );

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE member_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long save(CartItemEntity cartItemEntity) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("member_id", cartItemEntity.getMemberId());
        parameters.put("product_id", cartItemEntity.getProductId());
        parameters.put("quantity", cartItemEntity.getQuantity());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE id = ? ";
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

    public void updateQuantity(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }
}

