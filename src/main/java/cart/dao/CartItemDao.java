package cart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final RowMapper<CartItem> rowMapper = (rs, rowNum) ->
            new CartItem(
                    rs.getLong("cart_item.id"),
                    new Member(
                            rs.getLong("cart_item.member_id"),
                            rs.getString("email"),
                            null
                    ),
                    new Product(
                            rs.getLong("product.id"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getString("image_url")
                    ),
                    rs.getInt("cart_item.quantity")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public CartItem insert(final CartItem cartItem) {
        final Map<String, Object> params = new HashMap<>();
        params.put("member_id", cartItem.getMember().getId());
        params.put("product_id", cartItem.getProduct().getId());
        params.put("quantity", cartItem.getQuantity());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new CartItem(id, cartItem);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<CartItem> findById(final Long id) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
