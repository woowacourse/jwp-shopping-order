package com.woowahan.techcourse.cart.dao;

import com.woowahan.techcourse.cart.domain.CartItem;
import com.woowahan.techcourse.cart.dto.CartItemResponse;
import com.woowahan.techcourse.product.domain.Product;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemQueryDao {

    private final JdbcTemplate jdbcTemplate;

    public CartItemQueryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItemResponse> findByMemberId(long memberId) {
        String sql =
                "SELECT cart_item.id, cart_item.member_id, cart_item.quantity, "
                        + "product.id, product.name, product.price, product.image_url "
                        + "FROM cart_item "
                        + "INNER JOIN product ON cart_item.product_id = product.id "
                        + "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, getCartItemResponseRowMapper(memberId), memberId);
    }

    private RowMapper<CartItemResponse> getCartItemResponseRowMapper(long memberId) {
        return (rs, rowNum) -> {
            long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Product product = new Product(productId, name, price, imageUrl);
            CartItem cartItem = new CartItem(cartItemId, quantity, productId, memberId);
            return CartItemResponse.of(cartItem, product);
        };
    }
}
