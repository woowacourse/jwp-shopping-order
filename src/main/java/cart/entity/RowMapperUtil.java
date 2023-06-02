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

    public static final RowMapper<CartItemWithProductEntity> cartItemWithProductEntityRowMapper = (rs, rn) ->
            new CartItemWithProductEntity(
                    rs.getLong("cart_item.id"),
                    rs.getLong("cart_item.member_id"),
                    rs.getLong("product.id"),
                    rs.getString("product.name"),
                    rs.getInt("product.price"),
                    rs.getString("product.image_url"),
                    rs.getInt("cart_item.quantity")
            );

    public static final RowMapper<OrderEntity> orderEntityRowMapper = (rs, rn) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("original_price"),
            rs.getInt("discount_price"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public static final RowMapper<OrderItemWithProductEntity> orderItemWithProductEntityRowMapper = (rs, rn) ->
            new OrderItemWithProductEntity(
                    rs.getLong("order_item.id"),
                    rs.getLong("order_item.order_id"),
                    rs.getLong("product.id"),
                    rs.getString("product.name"),
                    rs.getInt("product.price"),
                    rs.getString("product.image_url"),
                    rs.getInt("order_item.quantity")
            );
}
