package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> {
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
        return new CartItem(cartItemId, quantity, member, product);
    };

    public Long save(final CartItem cartItem) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("quantity", cartItem.getQuantityValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public CartItem findById(final Long cartItemId) {
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id = ?";

        return jdbcTemplate.queryForObject(sql, cartItemRowMapper, cartItemId);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.member_id = ?";

        return jdbcTemplate.query(sql, new Object[]{memberId}, cartItemRowMapper);
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

        jdbcTemplate.update(sql, cartItem.getQuantityValue(), cartItem.getId());
    }
}

