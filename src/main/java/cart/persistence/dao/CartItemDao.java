package cart.persistence.dao;

import cart.persistence.entity.CartItemEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private static final RowMapper<CartItemEntity> MAPPER = (resultSet, rowNum) -> new CartItemEntity(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getLong("product_id"),
            resultSet.getInt("quantity")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            final long productId = rs.getLong("product.id");
            final long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("quantity");
            return new CartItemEntity(cartItemId, memberId, productId, quantity);
        });
    }

    public long createCartItem(final CartItemEntity cartItemEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", cartItemEntity.getMemberId());
        params.put("product_id", cartItemEntity.getProductId());
        params.put("quantity", cartItemEntity.getQuantity());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public CartItemEntity findById(final long id) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.grade, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        final List<CartItemEntity> cartItemEntities = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            long cartItemId = rs.getLong("id");
            long memberId = rs.getLong("member_id");
            long productId = rs.getLong("product.id");
            int quantity = rs.getInt("quantity");
            return new CartItemEntity(cartItemId, memberId, productId, quantity);
        });
        return cartItemEntities.isEmpty() ? null : cartItemEntities.get(0);
    }

    public List<CartItemEntity> findAllByIds(final List<Long> ids) {
        final String inQuery = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));
        final String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.product_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                        + "FROM cart_item "
                        + "INNER JOIN member ON cart_item.member_id = member.id "
                        + "INNER JOIN product ON cart_item.product_id = product.id "
                        + "WHERE cart_item.id IN (" + inQuery + ")";
        return jdbcTemplate.query(sql, MAPPER, ids.toArray());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(final CartItemEntity cartItemEntity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteByIds(final List<Long> ids) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, ids, ids.size(), ((ps, id) -> {
            ps.setLong(1, id);
        }));
    }
}
