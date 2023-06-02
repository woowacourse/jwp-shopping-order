package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemRepository {

    private static final String CART_ITEM_JOIN_MEMBER_SQL = "SELECT cart_item.id, cart_item.member_id, member.email, cart_item.product_id, product.product_name, product.price, product.image_url, cart_item.quantity " +
            "FROM cart_item " +
            "INNER JOIN member ON cart_item.member_id = member.id " +
            "INNER JOIN product ON cart_item.product_id = product.id ";
    private static final String WHERE_MEMBER_ID = "WHERE cart_item.member_id = ?";
    private static final String WHERE_CART_ITEM_ID = "WHERE cart_item.id = ?";

    private static final RowMapper<CartItem> cartItemRowMapper = ((rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        Long productId = rs.getLong("product_id");
        String name = rs.getString("product_name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("quantity");
        Member member = new Member(memberId, email, null);
        Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, quantity, product, member);
    });

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CartItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = CART_ITEM_JOIN_MEMBER_SQL + WHERE_MEMBER_ID;

        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public Long save(CartItem cartItem) {
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMember().getId())
                .addValue("product_id", cartItem.getProduct().getId())
                .addValue("product_name", cartItem.getProduct().getName())
                .addValue("price", cartItem.getProduct().getPrice())
                .addValue("image_url", cartItem.getProduct().getImageUrl())
                .addValue("quantity", cartItem.getQuantity());

        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public CartItem findById(Long id) {
        String sql = CART_ITEM_JOIN_MEMBER_SQL + WHERE_CART_ITEM_ID;

        return jdbcTemplate.queryForObject(sql, cartItemRowMapper, id);
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

