package cart.dao;

import cart.domain.CartItem;
import cart.domain.Product;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final static RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        Long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");
        Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, quantity, product, memberId);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, cart_item.quantity, product.id, product.name, product.price, product.image_url "
                        +
                        "FROM cart_item " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public Long save(CartItem cartItem) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", cartItem.getMemberId());
        parameters.put("product_id", cartItem.getProduct().getId());
        parameters.put("quantity", cartItem.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public Optional<CartItem> findById(Long id) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ? ";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.id = ? and  cart_item.member_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, memberId, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
