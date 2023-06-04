package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_items")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql =
                "SELECT cart_items.id, cart_items.quantity, "
                        + "products.id, products.name, products.price, products.image_url, "
                        + "members.id, members.email, members.password "
                        + "FROM cart_items "
                        + "INNER JOIN members ON cart_items.member_id = members.id "
                        + "INNER JOIN products ON cart_items.product_id = products.id "
                        + "WHERE members.id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper(), memberId);
    }

    public Long saveCartItem(final CartItem cartItem) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("quantity", cartItem.getQuantity());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public Optional<CartItem> findById(final Long id) {
        final String sql =
                "SELECT cart_items.id, cart_items.quantity, "
                        + "products.id, products.name, products.price, products.image_url, "
                        + "members.id, members.email, members.password "
                        + "FROM cart_items "
                        + "INNER JOIN members ON cart_items.member_id = members.id "
                        + "INNER JOIN products ON cart_items.product_id = products.id "
                        + "WHERE cart_items.id = ?";
        final List<CartItem> cartItems = jdbcTemplate.query(sql, cartItemRowMapper(), id);

        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cartItems.get(0));
    }

    public List<CartItem> findByIds(final List<Long> ids) {
        final String findByIdsQuery =
                "SELECT cart_items.id, cart_items.quantity, "
                        + "products.id, products.name, products.price, products.image_url, "
                        + "members.id, members.email, members.password "
                        + "FROM cart_items "
                        + "INNER JOIN members ON cart_items.member_id = members.id "
                        + "INNER JOIN products ON cart_items.product_id = products.id "
                        + "WHERE cart_items.id IN (:cartItemIds)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource("cartItemIds", ids);

        return namedParameterJdbcTemplate.query(findByIdsQuery, parameters, cartItemRowMapper());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteById(final List<Long> ids) {
        final String deleteByIdQuery = "DELETE FROM cart_items WHERE id IN (:cartItemIds)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource("cartItemIds", ids);

        namedParameterJdbcTemplate.update(deleteByIdQuery, parameters);
    }

    public void updateQuantity(final Long id, final int quantity) {
        final String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, id);
    }

    public List<CartItem> findAll() {
        final String sql =
                "SELECT cart_items.id, cart_items.quantity, products.id, products.name, products.price, products.image_url, members.id, members.email, members.password "
                        + "FROM cart_items "
                        + "INNER JOIN members ON cart_items.member_id = members.id "
                        + "INNER JOIN products ON cart_items.product_id = products.id";
        return jdbcTemplate.query(sql, cartItemRowMapper());
    }

    private RowMapper<CartItem> cartItemRowMapper() {
        return (rs, rowNum) -> {
            final Member member = memberMapper(rs);
            final Product product = productMapper(rs);

            final Long cartItemId = rs.getLong("id");
            final int quantity = rs.getInt("quantity");
            return new CartItem(cartItemId, quantity, product, member);
        };
    }

    private Member memberMapper(final ResultSet rs) throws SQLException {
        final Long memberId = rs.getLong("members.id");
        final String email = rs.getString("email");
        final String password = rs.getString("password");
        return new Member(memberId, email, password);
    }

    private Product productMapper(final ResultSet rs) throws SQLException {
        final Long productId = rs.getLong("products.id");
        final String name = rs.getString("name");
        final int price = rs.getInt("price");
        final String imageUrl = rs.getString("image_url");
        return new Product(productId, name, price, imageUrl);
    }
}

