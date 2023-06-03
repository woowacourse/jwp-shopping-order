package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("cart_item")
            .usingColumns("member_id", "product_id", "quantity")
            .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql = "SELECT "
            + "cart_item.id, cart_item.member_id, "
            + "member.email, "
            + "product.id, product.name, product.price, product.image_url, "
            + "cart_item.quantity "
            + "FROM cart_item "
            + "INNER JOIN member ON cart_item.member_id = member.id "
            + "INNER JOIN product ON cart_item.product_id = product.id "
            + "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final String email = rs.getString("email");
            final Long productId = rs.getLong("product.id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");
            final Long cartItemId = rs.getLong("cart_item.id");
            final int quantity = rs.getInt("cart_item.quantity");
            final Member member = new Member(memberId, email, null);
            final Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        }, memberId);
    }

    public CartItem save(final CartItem cartItem) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("member_id", cartItem.getMember().getId());
        params.addValue("product_id", cartItem.getProduct().getId());
        params.addValue("quantity", cartItem.getQuantity());

        final long persistedCartItemId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new CartItem(persistedCartItemId, cartItem.getQuantity(), cartItem.getProduct(), cartItem.getMember());
    }

    public Optional<CartItem> findById(final Long id) {
        final String sql = "SELECT "
            + "cart_item.id, cart_item.member_id, "
            + "member.email, "
            + "product.id, product.name, product.price, product.image_url, "
            + "cart_item.quantity "
            + "FROM cart_item "
            + "INNER JOIN member ON cart_item.member_id = member.id "
            + "INNER JOIN product ON cart_item.product_id = product.id "
            + "WHERE cart_item.id = ?";
        final List<CartItem> cartItems = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        }, id);

        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cartItems.get(0));
    }


    public void delete(final Long memberId, final Long productId) {
        final String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteByMemberIdAndProductIds(final Long memberId, final List<Long> productIds) {
        final String inSql = IntStream.range(0, productIds.size())
            .mapToObj((i) -> "?")
            .collect(Collectors.joining(", ", "(", ")"));

        final String sql = String.format("DELETE FROM cart_item WHERE member_id = %d AND product_id IN " + inSql,
            memberId);

        jdbcTemplate.update(sql, productIds.toArray());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

