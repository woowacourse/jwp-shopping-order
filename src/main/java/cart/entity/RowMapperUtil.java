package cart.entity;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperUtil {

    public static final RowMapper<MemberEntity> memberEntityRowMapper = (rs, rn) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    public static final RowMapper<ProductEntity> productEntityRowMapper = (rs, rn) -> new ProductEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );

    public static final RowMapper<CartItemDetailEntity> cartItemDetailEntityRowMapper = (rs, rn) -> new CartItemDetailEntity(
            rs.getLong("cart_item.id"),
            rs.getLong("member.id"),
            rs.getString("member.email"),
            rs.getLong("product.id"),
            rs.getString("product.name"),
            rs.getInt("product.price"),
            rs.getString("product.image_url"),
            rs.getInt("cart_item.quantity")
    );

    public static final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rn) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("total_price"),
            rs.getInt("discount_price")
    );
}
