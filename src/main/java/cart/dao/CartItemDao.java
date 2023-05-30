package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final String EMPTY_PASSWORD = null;
    private static final RowMapper<CartItem> ROW_MAPPER = (rs, rowNum) -> {
        Member member = new Member(
            rs.getLong("member.id"), rs.getString("email"), EMPTY_PASSWORD);
        Product product = new Product(
            rs.getLong("product.id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url"));
        return new CartItem(
            rs.getLong("cart_item.id"),
            rs.getInt("cart_item.quantity"),
            product, member);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("cart_item")
            .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql =
            "SELECT cart_item.id, cart_item.member_id, member.id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public Long save(CartItem cartItem) {
        Number generatedKey = insertAction.executeAndReturnKey(
            Map.of("member_id", cartItem.getMember().getId(),
                "product_id", cartItem.getProduct().getId(),
                "quantity", cartItem.getQuantity()));

        return Objects.requireNonNull(generatedKey).longValue();
    }

    public CartItem findById(Long id) {
        String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, member.id, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }


    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

