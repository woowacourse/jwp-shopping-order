package cart.dao;

import cart.entity.CartItemEntity;
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
                .usingGeneratedKeyColumns("id");
    }

    public Optional<CartItemEntity> getById(Long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE id = ?";
        try {
            CartItemEntity cartItem = jdbcTemplate.queryForObject(sql, new CartItemRowMapper(), id);
            return Optional.ofNullable(cartItem);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<CartItemEntity> getAllByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, new CartItemRowMapper(), memberId);
    }

    public Long insert(CartItemEntity entity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public void update(Long cartId, Integer quantity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, cartId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAllByMemberId(Long memberId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ?";
        jdbcTemplate.update(sql, memberId);
    }

    private static class CartItemRowMapper implements RowMapper<CartItemEntity> {
        @Override
        public CartItemEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItemEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity")
            );
        }
    }
}
