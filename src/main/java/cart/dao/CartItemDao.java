package cart.dao;

import cart.entity.CartItemEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity")
            );

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(CartItemEntity cartItemEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItemEntity.getMemberId());
            ps.setLong(2, cartItemEntity.getProductId());
            ps.setInt(3, cartItemEntity.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItemEntity findById(Long id) {
        final String sql = "SELECT * FROM cart_item WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        final String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public void updateQuantity(CartItemEntity cartItemEntity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
