package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<CartItem> rowMapper = (rs, rowNum) -> {
        long memberId = rs.getLong("member_id");
        String email = rs.getString("member.email");
        Long productId = rs.getLong("product.id");
        String name = rs.getString("product.name");
        int price = rs.getInt("product.price");
        String imageUrl = rs.getString("product.image_url");
        Long cartItemId = rs.getLong("id");
        int quantity = rs.getInt("quantity");
        long point = rs.getLong("member_point.point");
        Member member = new Member(memberId, email, null, point);
        Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, quantity, product, member);
    };

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity, member_point.point " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "INNER JOIN member_point ON cart_item.member_id = member_point.member_id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long save(CartItem cartItem) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("member_id", cartItem.getMember().getId());
        mapSqlParameterSource.addValue("product_id", cartItem.getProduct().getId());
        mapSqlParameterSource.addValue("quantity", cartItem.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    public Optional<CartItem> findById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity, member_point.point " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "INNER JOIN member_point ON cart_item.member_id = member_point.member_id " +
                "WHERE cart_item.id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findAny();
    }

    public void deleteByMemberAndProduct(Long memberId, Long productId) {
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

