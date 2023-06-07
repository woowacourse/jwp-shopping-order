package cart.cartitem.dao;

import cart.cartitem.domain.CartItem;
import cart.cartitem.repository.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getLong("quantity")
            );
    
    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }
    
    public List<CartItemEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }
    
    public Long save(CartItemEntity cartItemEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", cartItemEntity.getMemberId());
        params.put("product_id", cartItemEntity.getProductId());
        params.put("quantity", cartItemEntity.getQuantity());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public List<CartItemEntity> findByIds(final List<Long> cartItemIds) {
        final String inClause = String.join(",", Collections.nCopies(cartItemIds.size(), "?"));
        String sql = "SELECT * FROM cart_item WHERE id IN (" + inClause + ")";
        return jdbcTemplate.query(sql, rowMapper, cartItemIds.toArray());
    }
    
    public CartItemEntity findById(Long id) {
        final String sql = "SELECT * FROM cart_item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    
    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }
    
    public void update(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET member_id = ?, product_id = ?, quantity = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                cartItemEntity.getMemberId(),
                cartItemEntity.getProductId(),
                cartItemEntity.getQuantity(),
                cartItemEntity.getId()
        );
    }
    
    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public void deleteByProductId(final Long productId) {
        String sql = "DELETE FROM cart_item WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}

