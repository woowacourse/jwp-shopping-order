package cart.dao;

import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public CartItemDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource,
                       final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("cart_item")
                .usingColumns("member_id", "product_id", "quantity")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final RowMapper<CartItem> cartItemRowMapper = (result, rowNum) -> {
        final Long memberId = result.getLong("member.id");
        final String email = result.getString("member.email");
        final Member member = new Member(memberId, email, null);

        final Long productId = result.getLong("product.id");
        final String productName = result.getString("product.name");
        final int productPrice = result.getInt("product.price");
        final String productImageUrl = result.getString("product.image_url");
        final Product product = new Product(productId, productName, productPrice, productImageUrl);

        final Long cartItemId = result.getLong("cart_item.id");
        final int quantity = result.getInt("cart_item.quantity");
        return new CartItem(cartItemId, member, product, quantity);
    };

    public Long save(final CartItem cartItem) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMemberId())
                .addValue("product_id", cartItem.getProductId())
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

        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }


    public List<CartItem> getCartItemsByIds(final List<Long> cartItemIds) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", cartItemIds);
        final String sql = "SELECT ci.id, ci.member_id, m.id, m.email, " +
                "p.id, p.name, p.price, p.image_url, ci.quantity " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id IN (:ids)";

        return namedParameterJdbcTemplate.query(sql, parameters, cartItemRowMapper);
    }

    public void deleteById(final Long cartItemId) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.update(sql, cartItemId);
    }

    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";

        jdbcTemplate.update(sql, cartItem.getQuantityValue(), cartItem.getId());
    }
}

