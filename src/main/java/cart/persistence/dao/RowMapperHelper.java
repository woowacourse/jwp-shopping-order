package cart.persistence.dao;

import cart.persistence.dto.CartDetailDTO;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperHelper {

    private RowMapperHelper() {
    }

    public static RowMapper<CartItemEntity> cartItemRowMapper() {
        return getCartItemRowMapper(false);
    }

    private static RowMapper<CartItemEntity> cartItemRowMapperWithTable() {
        return getCartItemRowMapper(true);
    }

    private static RowMapper<CartItemEntity> getCartItemRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "cart_item." : "";
            return new CartItemEntity(
                    rs.getLong(prefix + "id"),
                    rs.getLong(prefix + "member_id"),
                    rs.getLong(prefix + "product_id"),
                    rs.getInt(prefix + "quantity")
            );
        };
    }

    public static RowMapper<MemberEntity> memberRowMapper() {
        return getMemberRowMapper(false);
    }

    private static RowMapper<MemberEntity> memberRowMapperWithTable() {
        return getMemberRowMapper(true);
    }

    private static RowMapper<MemberEntity> getMemberRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "member." : "";
            return new MemberEntity(
                    rs.getLong(prefix + "id"),
                    rs.getString(prefix + "email"),
                    rs.getString(prefix + "password")
            );
        };
    }

    public static RowMapper<ProductEntity> productRowMapper() {
        return getProductRowMapper(false);
    }

    private static RowMapper<ProductEntity> productRowMapperWithTable() {
        return getProductRowMapper(true);
    }

    private static RowMapper<ProductEntity> getProductRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "product." : "";
            return new ProductEntity(
                    rs.getLong(prefix + "id"),
                    rs.getString(prefix + "name"),
                    rs.getInt(prefix + "price"),
                    rs.getString(prefix + "image_url")
            );
        };
    }

    public static RowMapper<CartDetailDTO> cartDetailRowMapper() {
        return (rs, rowNum) -> {
            CartItemEntity cartItem = cartItemRowMapperWithTable().mapRow(rs, rowNum);
            MemberEntity member = memberRowMapperWithTable().mapRow(rs, rowNum);
            ProductEntity product = productRowMapperWithTable().mapRow(rs, rowNum);
            return new CartDetailDTO(cartItem, member, product);
        };
    }
}
