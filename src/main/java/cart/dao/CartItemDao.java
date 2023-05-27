package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql = "SELECT m.email, " +
                "p.id, p.name, p.price, p.image_url, " +
                "ci.id, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            final String email = rs.getString("email");
            final Member member = new Member(memberId, email, null);

            final Long productId = rs.getLong("product.id");
            final String productName = rs.getString("name");
            final int productPrice = rs.getInt("price");
            final String productImageUrl = rs.getString("image_url");
            final Product product = new Product(productId, productName, productPrice, productImageUrl);

            final Long cartItemId = rs.getLong("cart_item.id");
            final int quantity = rs.getInt("quantity");
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public Long save(final CartItem cartItem) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("quantity", cartItem.getQuantity());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public CartItem findById(final Long id) {
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id = ?";
        final List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            final Long memberId = rs.getLong("member.id");
            final String email = rs.getString("email");
            final Member member = new Member(memberId, email, null);

            final Long productId = rs.getLong("product.id");
            final String productName = rs.getString("name");
            final int productPrice = rs.getInt("price");
            final String productImageUrl = rs.getString("image_url");
            final Product product = new Product(productId, productName, productPrice, productImageUrl);

            final Long cartItemId = rs.getLong("cart_item.id");
            final int quantity = rs.getInt("quantity");
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
}

