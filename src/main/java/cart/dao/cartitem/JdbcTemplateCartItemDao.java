package cart.dao.cartitem;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateCartItemDao implements CartItemDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateCartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<CartItem> findCartItemById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, product.stock, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            Long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            Long stock = rs.getLong("stock");
            Long cartItemId = rs.getLong("cart_item.id");
            Long quantity = rs.getLong("cart_item.quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl, stock);
            return new CartItem(cartItemId, quantity, member, product);
        });
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cartItems.get(0));
    }

    @Override
    public Optional<CartItem> findCartItemByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, product.stock, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE member.id = ? and product.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{memberId, productId}, (rs, rowNum) -> {
            Long findMemberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long findProductId = rs.getLong("product.id");
            String name = rs.getString("name");
            Long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            Long stock = rs.getLong("stock");
            Long cartItemId = rs.getLong("cart_item.id");
            Long quantity = rs.getLong("cart_item.quantity");
            Member member = new Member(findMemberId, email, null);
            Product product = new Product(findProductId, name, price, imageUrl, stock);
            return new CartItem(cartItemId, quantity, member, product);
        });

        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cartItems.get(0));
    }

    @Override
    public List<CartItem> findAllCartItemsByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, product.stock, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            Long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            Long stock = rs.getLong("stock");
            Long cartItemId = rs.getLong("cart_item.id");
            Long quantity = rs.getLong("cart_item.quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl, stock);
            return new CartItem(cartItemId, quantity, member, product);
        });
    }

    @Override
    public List<Long> findAllCartIdsByMemberId(Long memberId) {
        String sql = "SELECT id FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) ->
                rs.getLong("id")
        );
    }

    @Override
    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setLong(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }
}
