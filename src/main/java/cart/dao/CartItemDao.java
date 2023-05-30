package cart.dao;

import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemDao {
    private static final RowMapper<CartItem> CART_ITEM_MAPPER = (rs, rowNum) -> {
        final Member member = new Member(
                rs.getLong("member_id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("point")
        );
        final Product product = new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
        return new CartItem(
                rs.getLong("cart_item.id"),
                member,
                product,
                rs.getInt("cart_item.quantity")
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.password, member.point, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, CART_ITEM_MAPPER, memberId);
    }

    public Long save(final CartItem cartItem) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("quantity", cartItem.getQuantity());
        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public CartItem findById(final Long id) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.password, member.point, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        return jdbcTemplate.queryForObject(sql, CART_ITEM_MAPPER, id);
    }


    public List<CartItem> findByIds(final List<Long> cartIds) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.password, member.point, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id IN (:id)";
        final MapSqlParameterSource source = new MapSqlParameterSource("id", cartIds);
        return namedJdbcTemplate.query(sql, source, CART_ITEM_MAPPER);
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
