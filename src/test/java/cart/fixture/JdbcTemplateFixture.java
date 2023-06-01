package cart.fixture;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTemplateFixture {

    public static void insertMember(Member member, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO member (id, email, password) VALUES (?, ?,?)";
        jdbcTemplate.update(sql, member.getId(), member.getEmail(), member.getPassword());
    }

    public static void insertCoupon(Coupon coupon, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO coupon (id, name, type, discount_amount) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, coupon.getId(), coupon.getName(), coupon.getType().name(), coupon.getDiscountAmount());
    }

    public static void insertProduct(Product product, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO Product (id, name, price, image_url) VALUES (?, ?,?,?)";
        jdbcTemplate.update(sql, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public static void insertOrder(OrderEntity orderEntity, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO orders (id, member_id) VALUES (?,?)";
        jdbcTemplate.update(sql, orderEntity.getId(), orderEntity.getMemberId());
    }

    public static void insertOrderItem(OrderItemEntity orderItemEntity, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO order_item (id, order_id, product_id, quantity, product_name, product_price, product_image_url) VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                orderItemEntity.getId(),
                orderItemEntity.getOrderId(),
                orderItemEntity.getProductId(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getProductName(),
                orderItemEntity.getProductPrice(),
                orderItemEntity.getProductImageUrl());
    }

    public static void insertMemberCoupon(Member member, Coupon coupon, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO member_coupon(member_id, coupon_id, used) VALUES (?,?, ?)";
        jdbcTemplate.update(sql, member.getId(), coupon.getId(), false);
    }

    public static void insertUsedMemberCoupon(Member member, Coupon coupon, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO member_coupon(member_id, coupon_id, used) VALUES (?,?, ?)";
        jdbcTemplate.update(sql, member.getId(), coupon.getId(), true);
    }
}
