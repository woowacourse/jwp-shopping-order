package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<CartItem> rowMapper = (rs, rowNum) -> {
        final Long memberId = rs.getLong("member_id");
        final String email = rs.getString("email");
        final Long productId = rs.getLong("product.id");
        final String name = rs.getString("name");
        final int price = rs.getInt("price");
        final String imageUrl = rs.getString("image_url");
        final Long cartItemId = rs.getLong("cart_item.id");
        final int quantity = rs.getInt("cart_item.quantity");
        final Member member = new Member(memberId, email, null);
        final Product product = new Product(productId, name, imageUrl, price);
        return new CartItem(cartItemId, quantity, member, product);
    };

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingColumns("quantity", "member_id", "product_id")
                .usingGeneratedKeyColumns("id");
    }

    public CartItem save(final CartItem cartItem) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("quantity", cartItem.getQuantity());
        params.addValue("member_id", cartItem.getMember().getId());
        params.addValue("product_id", cartItem.getProduct().getId());
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new CartItem(id, cartItem.getQuantity(), cartItem.getMember(), cartItem.getProduct());
    }

    public List<CartItem> findAllByMemberId(final Long memberId) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<CartItem> findById(Long id) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
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

    public int delete(final Long cartItemId, final Long memberId) {
        final String sql = "DELETE FROM cart_item WHERE id = ? AND member_id = ?";
        return jdbcTemplate.update(sql, cartItemId, memberId);
    }

    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}
