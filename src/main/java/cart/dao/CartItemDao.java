package cart.dao;

import cart.entity.CartItemEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) ->
            CartItemEntity.of(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id"),
                    rs.getInt("quantity")
            );

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long create(final CartItemEntity cartItem) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", cartItem.getMemberId());
        params.put("product_id", cartItem.getProductId());
        params.put("quantity", cartItem.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<CartItemEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<CartItemEntity> findById(final Long id) {
        final String sql = "SELECT * FROM cart_item WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<CartItemEntity> findByMemberIdAndProductId(final Long memberId, final Long productId) {
        final String sql = "SELECT * FROM cart_item WHERE member_id = ? AND product_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, memberId, productId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Optional<CartItemEntity>> findAllByIds(final List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public void updateQuantity(final CartItemEntity cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAll(final List<CartItemEntity> cartItems) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, cartItems.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return cartItems.size();
            }
        });
    }
}
