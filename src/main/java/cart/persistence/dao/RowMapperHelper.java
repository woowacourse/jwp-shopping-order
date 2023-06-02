package cart.persistence.dao;

import cart.domain.coupon.CouponType;
import cart.persistence.dto.CartDetailDTO;
import cart.persistence.dto.MemberCouponDetailDTO;
import cart.persistence.dto.OrderDetailDTO;
import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.OrderItemEntity;
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
                    rs.getString(prefix + "password"),
                    rs.getString(prefix + "nickname")
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


    public static RowMapper<CouponEntity> couponRowMapper() {
        return getCouponRowMapper(false);
    }

    private static RowMapper<CouponEntity> couponRowMapperWithTable() {
        return getCouponRowMapper(true);
    }

    private static RowMapper<CouponEntity> getCouponRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "coupon." : "";
            return new CouponEntity(
                    rs.getLong(prefix + "id"),
                    rs.getString(prefix + "name"),
                    rs.getInt(prefix + "min_price"),
                    rs.getInt(prefix + "max_price"),
                    CouponType.valueOf(rs.getString(prefix + "type")),
                    (Integer) rs.getObject("discount_amount"),
                    (Double) rs.getObject("discount_percentage")
            );
        };
    }

    public static RowMapper<MemberCouponEntity> memberCouponRowMapper() {
        return getMemberCouponRowMapper(true);
    }

    private static RowMapper<MemberCouponEntity> memberCouponRowMapperWithTable() {
        return getMemberCouponRowMapper(false);
    }

    private static RowMapper<MemberCouponEntity> getMemberCouponRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "member_coupon." : "";
            return new MemberCouponEntity(
                    rs.getLong(prefix + "id"),
                    rs.getLong(prefix + "member_id"),
                    rs.getLong(prefix + "coupon_id"),
                    rs.getBoolean(prefix + "is_used"),
                    rs.getTimestamp("expired_at"),
                    rs.getTimestamp("created_at")
            );
        };
    }

    public static RowMapper<MemberCouponDetailDTO> memberCouponDetailRowMapper() {
        return (rs, rowNum) -> {
            MemberCouponEntity memberCoupon = memberCouponRowMapperWithTable().mapRow(rs, rowNum);
            CouponEntity coupon = couponRowMapperWithTable().mapRow(rs, rowNum);
            MemberEntity member = memberRowMapperWithTable().mapRow(rs, rowNum);
            return new MemberCouponDetailDTO(memberCoupon, coupon, member);
        };
    }

    public static RowMapper<OrderEntity> orderRowMapper() {
        return getOrderRowMapper(false);
    }

    private static RowMapper<OrderEntity> orderRowMapperWithTable() {
        return getOrderRowMapper(true);
    }

    private static RowMapper<OrderEntity> getOrderRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "order." : "";
            return new OrderEntity(
                    rs.getLong(prefix + "id"),
                    rs.getLong(prefix + "member_id"),
                    rs.getLong(prefix + "member_coupon_id"),
                    rs.getInt(prefix + "shipping_fee"),
                    rs.getInt(prefix + "total_price"),
                    rs.getTimestamp(prefix + "created_at")
            );
        };
    }

    public static RowMapper<OrderDetailDTO> orderDetailRowMapper() {
        return (rs, rowNum) -> {
            OrderEntity order = orderRowMapperWithTable().mapRow(rs, rowNum);
            MemberCouponEntity memberCoupon = memberCouponRowMapperWithTable().mapRow(rs, rowNum);
            MemberEntity member = memberRowMapperWithTable().mapRow(rs, rowNum);
            return new OrderDetailDTO(order, member, memberCoupon);
        };
    }

    public static RowMapper<OrderItemEntity> orderItemRowMapper() {
        return getOrderItemRowMapper(false);
    }

    private static RowMapper<OrderItemEntity> orderItemRowMapperWithTable() {
        return getOrderItemRowMapper(true);
    }

    private static RowMapper<OrderItemEntity> getOrderItemRowMapper(boolean withTable) {
        return (rs, rowNum) -> {
            String prefix = withTable ? "order_item." : "";
            return new OrderItemEntity(
                    rs.getLong(prefix + "id"),
                    rs.getLong(prefix + "order_id"),
                    rs.getLong(prefix + "product_id"),
                    rs.getString(prefix + "name"),
                    rs.getInt(prefix + "price"),
                    rs.getString(prefix + "image_url"),
                    rs.getInt(prefix + "quantity")
            );
        };
    }

}
