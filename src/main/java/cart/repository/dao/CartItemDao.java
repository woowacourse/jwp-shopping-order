package cart.repository.dao;

import cart.entity.CartItemEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity " +
                "FROM cart_item " +
                "WHERE member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, cartItemMapper);
    }

    public Long save(CartItemEntity cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMemberId());
            ps.setLong(2, cartItem.getProductId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemMapper, id));
        } catch (EmptyResultDataAccessException e) {
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

    public void deleteByIds(List<Long> cartItemIds) {
        String sql = "DELETE FROM cart_item WHERE id IN (:ids)";

        Map<String, Object> params = Collections.singletonMap("ids", cartItemIds);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void updateQuantity(CartItemEntity cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public List<CartItemEntity> findByIds(final List<Long> cartItemIds) {
        String sql = "SELECT id, member_id, product_id, quantity " +
                "FROM cart_item " +
                "WHERE id IN (:ids)";
        Map<String, Object> params = Collections.singletonMap("ids", cartItemIds);

        return namedParameterJdbcTemplate.query(sql, params, cartItemMapper);
    }

    public Page<CartItemEntity> getCartItemsByMemberId(final Pageable pageable, final Long memberId) {
        final int pageSize = pageable.getPageSize();
        final long offset = pageable.getOffset();

        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item "
                + "WHERE member_id = ? "
                + "LIMIT ? "
                + "OFFSET ?";

        final List<CartItemEntity> cartItems = jdbcTemplate.query(sql, new Object[]{memberId, pageSize, offset},
                cartItemMapper);

        String countSql = "SELECT COUNT(*) FROM cart_item WHERE member_id = ?";
        final Integer totalCartItemCount = jdbcTemplate.queryForObject(countSql, Integer.class, memberId);

        return new PageImpl<>(cartItems, pageable, totalCartItemCount);
    }

    private final RowMapper<CartItemEntity> cartItemMapper = (rs, rowNum) -> {
        Long cartItemId = rs.getLong("id");
        Long memberId = rs.getLong("member_id");
        Long productId = rs.getLong("product_id");
        int quantity = rs.getInt("quantity");
        return new CartItemEntity(cartItemId, memberId, productId, quantity);
    };
}

