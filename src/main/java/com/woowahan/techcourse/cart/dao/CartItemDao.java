package com.woowahan.techcourse.cart.dao;

import com.woowahan.techcourse.cart.domain.CartItem;
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

    public Optional<CartItem> findById(long id) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE cart_item.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<CartItem> findByIdAndMemberId(long id, long memberId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE cart_item.id = ? AND member_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id, memberId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(CartItem cartItem) {
        String sql = "UPDATE cart_item SET member_id = ?, product_id = ?, quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getMemberId(), cartItem.getProductId(), cartItem.getQuantity(),
                cartItem.getId());
    }

    public void deleteAll(Long memberId, List<Long> productIds) {
        String sql = "DELETE FROM cart_item WHERE member_id = (:memberId) AND product_id IN (:productIds)";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("productIds", productIds);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public void deleteByIdAndMemberId(long id, long memberId) {
        String sql = "DELETE FROM cart_item WHERE id = ? AND member_id = ?";
        jdbcTemplate.update(sql, id, memberId);
    }
}
