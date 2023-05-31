package cart.entity;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperUtil {

    public static final RowMapper<MemberEntity> memberEntityRowMapper = (rs, rn) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    public static final RowMapper<CartItemDetailEntity> cartItemDetailEntityRowMapper = (rs, rn) -> new CartItemDetailEntity(
            rs.getLong("id"),
            rs.getLong("member.id"),
            rs.getString("member.email"),
            rs.getLong("product.id"),
            rs.getString("product.name"),
            rs.getInt("product.price"),
            rs.getString("product.image_url"),
            rs.getInt("quantity")
    );

    public static final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rn) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("total_price"),
            rs.getInt("discount_price")
    );
}
