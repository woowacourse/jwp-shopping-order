package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT c.id, c.member_id, m.email, p.id, p.name, p.price, p.image_url, c.quantity " +
                "FROM cart_items AS c " +
                "INNER JOIN members AS m ON c.member_id = m.id " +
                "INNER JOIN products AS p ON c.product_id = p.id " +
                "WHERE c.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            Member member = new Member(memberId, email, null);
            Long productId = rs.getLong("products.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Product product = new Product(productId, name, price, imageUrl);
            Long cartItemId = rs.getLong("cart_items.id");
            int quantity = rs.getInt("cart_items.quantity");
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_items (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItem findById(Long id) {
        String sql = "SELECT c.id, c.member_id, m.email, p.id, p.name, p.price, p.image_url, c.quantity " +
                "FROM cart_items AS c " +
                "INNER JOIN members AS m ON c.member_id = m.id " +
                "INNER JOIN products AS p ON c.product_id = p.id " +
                "WHERE c.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Member member = new Member(memberId, email, null);
            Long productId = rs.getLong("products.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Product product = new Product(productId, name, price, imageUrl);
            Long cartItemId = rs.getLong("cart_items.id");
            int quantity = rs.getInt("cart_items.quantity");
            return new CartItem(cartItemId, quantity, product, member);
        });
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }

    public List<CartItem> findByIds(List<Long> ids) {
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));

        String sql = "SELECT c.id, c.member_id, m.email, p.id, p.name, p.price, p.image_url, c.quantity " +
                "FROM cart_items AS c " +
                "INNER JOIN members AS m ON c.member_id = m.id " +
                "INNER JOIN products AS p ON c.product_id = p.id " +
                "WHERE c.id IN (" + placeholders + ")";
        return jdbcTemplate.query(sql, ids.toArray(), (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Member member = new Member(memberId, email, null);
            Long productId = rs.getLong("products.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Product product = new Product(productId, name, price, imageUrl);
            Long cartItemId = rs.getLong("cart_items.id");
            int quantity = rs.getInt("cart_items.quantity");
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

