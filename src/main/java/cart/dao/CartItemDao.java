package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Price;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        Long productId = rs.getLong("product.id");
        String name = rs.getString("name");
        Price price = new Price(rs.getInt("price"));
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("cart_item.id");
        Integer quantity = rs.getInt("cart_item.quantity");
        Member member = new Member(memberId, email, null);
        Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, quantity, product, member);
    };

    public CartItemDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "product_id", "quantity");
    }

    public Long save(CartItem cartItem) {
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", cartItem.getMember().getId());
        params.put("product_id", cartItem.getProduct().getId());
        params.put("quantity", cartItem.getQuantity());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public Optional<CartItem> findById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id as product_id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, id));
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
