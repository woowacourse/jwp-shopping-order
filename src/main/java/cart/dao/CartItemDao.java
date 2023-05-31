package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.vo.Amount;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            final String email = rs.getString("email");
            final Long productId = rs.getLong("product.id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");
            final Long cartItemId = rs.getLong("cart_item.id");
            final int quantity = rs.getInt("cart_item.quantity");
            final Member member = new Member(memberId, email, null);
            final Product product = new Product(productId, name, Amount.of(price), imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public Long save(final CartItem cartItem) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(
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
        final String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        final List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            final Long memberId = rs.getLong("member_id");
            final String email = rs.getString("email");
            final Long productId = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");
            final Long cartItemId = rs.getLong("cart_item.id");
            final int quantity = rs.getInt("cart_item.quantity");
            final Member member = new Member(memberId, email, null);
            final Product product = new Product(productId, name, Amount.of(price), imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
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

    public Optional<CartItem> findByMemberIdAndProductId(final Long memberId, final Long productId) {
        final String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ? AND cart_item.product_id = ?";

        final List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{memberId, productId}, (rs, rowNum) -> {
            final Long findMemberId = rs.getLong("member_id");
            final String email = rs.getString("email");
            final Long findProductId = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");
            final Long cartItemId = rs.getLong("cart_item.id");
            final int quantity = rs.getInt("cart_item.quantity");
            final Member member = new Member(findMemberId, email, null);
            final Product product = new Product(findProductId, name, Amount.of(price), imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
        return cartItems.stream().findAny();
    }

    public List<CartItem> findAllByIds(final List<Long> cartItemIds) {
        return cartItemIds.stream()
            .map(this::findById)
            .collect(Collectors.toList());
    }

    public void deleteAll(final List<CartItem> cartItems) {
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
