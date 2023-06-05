package cart.persistence.dao;

import cart.persistence.entity.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingColumns("quantity", "product_id", "member_id")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findCartItemsByMemberId(Long memberId) {
        String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, new CartItemEntityRowMapper());
    }

    public Long add(CartItemEntity cartItem) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(cartItem);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT * FROM cart_item WHERE id = ?";
        List<CartItemEntity> cartItems = jdbcTemplate.query(sql, new Object[]{id}, new CartItemEntityRowMapper());
        return cartItems.isEmpty() ? Optional.empty() : Optional.ofNullable(cartItems.get(0));
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

    private static class CartItemEntityRowMapper implements RowMapper<CartItemEntity> {
        @Override
        public CartItemEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItemEntity(
                    rs.getLong("id"),
                    rs.getInt("quantity"),
                    rs.getLong("product_id"),
                    rs.getLong("member_id")
            );
        }
    }
}

