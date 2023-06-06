package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertCartItems;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.insertCartItems = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
    }

    public CartItem findById(Long id) {
        String sql = "SELECT * " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";

        return jdbcTemplate.queryForObject(sql, new CartItemRowMapper(), id);
    }

    public List<CartItem> findByIds(List<Long> ids) {
        StringBuilder sql = new StringBuilder("SELECT * " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id IN (");

        StringJoiner placeholders = new StringJoiner(", ");
        for (int i = 0; i < ids.size(); i++) {
            placeholders.add("?");
        }
        sql.append(placeholders).append(")");

        return jdbcTemplate.query(sql.toString(), new CartItemRowMapper(), ids.toArray());
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT * " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";

        return jdbcTemplate.query(sql, new CartItemRowMapper(), memberId);
    }

    public Long save(CartItem cartItem) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("member_id", cartItem.getMember().getId());
        parameters.put("product_id", cartItem.getProduct().getId());
        parameters.put("quantity", cartItem.getQuantity());

        return insertCartItems.executeAndReturnKey(parameters).longValue();
    }


    public int updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public int delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        return jdbcTemplate.update(sql, memberId, productId);
    }

    public int deleteByMemberId(Long memberId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.update(sql, memberId);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByIds(List<Long> ids) {
        StringBuilder sql = new StringBuilder("DELETE FROM cart_item WHERE id In (");

        StringJoiner placeholders = new StringJoiner(", ");
        for (int i = 0; i < ids.size(); i++) {
            placeholders.add("?");
        }
        sql.append(placeholders).append(")");

        return jdbcTemplate.update(sql.toString(), ids.toArray());
    }

    private static class CartItemRowMapper implements RowMapper<CartItem> {
        @Override
        public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long memberId = rs.getLong("cart_item.member_id");
            String email = rs.getString("member.email");
            Long point = rs.getLong("member.point");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("product.name");
            Long price = rs.getLong("product.price");
            Double pointRatio = rs.getDouble("product.point_ratio");
            boolean pointAvailable = rs.getBoolean("product.point_available");
            String imageUrl = rs.getString("product.image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            Long quantity = rs.getLong("cart_item.quantity");
            Member member = new Member(memberId, email, point);
            Product product = new Product(productId, name, price.intValue(), imageUrl, pointRatio, pointAvailable);
            return new CartItem(cartItemId, quantity, product, member);
        }
    }
}

