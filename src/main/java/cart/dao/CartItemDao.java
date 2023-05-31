package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.Member;
import cart.domain.product.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.cash, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ? " +
                "ORDER BY cart_item.id DESC";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            Long cash = rs.getLong("cash");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = Member.of(memberId, email, null, cash);
            Product product = Product.of(productId, name, price, imageUrl);
            return CartItem.of(cartItemId, quantity, product, member);
        });
    }

    public CartItem findByMemberIdAndProductId(final Long memberId, final Long productId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.cash, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ? AND product.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{memberId, productId}, (rs, rowNum) -> {
                String email = rs.getString("email");
                Long cash = rs.getLong("cash");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                String imageUrl = rs.getString("image_url");
                Long cartItemId = rs.getLong("cart_item.id");
                int quantity = rs.getInt("cart_item.quantity");
                Member member = Member.of(memberId, email, null, cash);
                Product product = Product.of(productId, name, price, imageUrl);
                return CartItem.of(cartItemId, quantity, product, member);
            });
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }

    }

    public Long save(final CartItem cartItem) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItem findById(final Long id) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.cash, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        final List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long cash = rs.getLong("cash");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = Member.of(memberId, email, null, cash);
            Product product = Product.of(productId, name, price, imageUrl);
            return CartItem.of(cartItemId, quantity, product, member);
        });
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }

    public void delete(final Long memberId, final Long productId) {
        final String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
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

