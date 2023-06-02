package com.woowahan.techcourse.cart.dao;

import com.woowahan.techcourse.cart.domain.CartItem;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItem> ROW_MAPPER = (rs, rowNum) -> {
        long memberId = rs.getLong("member_id");
        long productId = rs.getLong("cart_item.product_id");
        long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");
        return new CartItem(cartItemId, quantity, productId, memberId);
    };
    
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(CartItem cartItem) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("member_id", cartItem.getMemberId())
                .addValue("product_id", cartItem.getProductId())
                .addValue("quantity", cartItem.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    public CartItem findById(Long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE cart_item.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, ROW_MAPPER, id);
        return cartItems.isEmpty() ? null : cartItems.get(0);
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

    public void deleteAll(Long memberId, List<Long> cartItemIds) {
        String sql = "DELETE FROM cart_item WHERE member_id = (:memberId) AND id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("ids", cartItemIds);
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
