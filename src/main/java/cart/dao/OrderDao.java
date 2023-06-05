package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;
import cart.exception.ForbiddenException;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Order> findByMemberId(Long memberId) {
        String sql = "SELECT o.id AS order_id, " +
            "o.member_id, " +
            "m.email, " +
            "m.password, " +
            "m.nickname, " +
            "oi.id AS item_id, " +
            "oi.product_id, " +
            "oi.name, " +
            "oi.price, " +
            "oi.image_url, " +
            "oi.quantity, " +
            "mc.id AS member_coupon_id, " +
            "mc.is_used, " +
            "mc.expired_at, " +
            "c.id AS coupon_id, " +
            "c.name AS coupon_name, " +
            "c.min_order_price, " +
            "c.max_discount_price, " +
            "c.type, " +
            "c.discount_amount, " +
            "c.discount_percentage " +
            "FROM `order` o " +
            "JOIN member m ON o.member_id = m.id " +
            "JOIN order_item oi ON o.id = oi.order_id " +
            "LEFT JOIN member_coupon mc ON o.member_coupon_id = mc.id " +
            "LEFT JOIN coupon c ON mc.coupon_id = c.id " +
            "WHERE m.id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, memberId);

        return getOrders(rows);
    }

    private List<Order> getOrders(List<Map<String, Object>> rows) {
        Map<Long, Order> orderMap = new HashMap<>();
        addOrders(rows, orderMap);
        addOrderItems(rows, orderMap);
        return new ArrayList<>(orderMap.values());
    }

    private void addOrders(List<Map<String, Object>> rows, Map<Long, Order> orderMap) {
        for (Map<String, Object> row : rows) {
            Long orderId = (Long)row.get("order_id");
            Order order = orderMap.get(orderId);

            addOrderIfNotAddedBefore(orderMap, row, orderId, order);
        }
    }

    private void addOrderIfNotAddedBefore(
        Map<Long, Order> orderMap,
        Map<String, Object> row,
        Long orderId,
        Order order
    ) {
        if (order == null) {
            Member member = getMember(row);
            Order newOrder = getOrder(row, orderId, member);

            orderMap.put(orderId, newOrder);
        }
    }

    private Order getOrder(Map<String, Object> row, Long orderId, Member member) {
        Order order = new Order(orderId, member, new ArrayList<>(), null);

        Long memberCouponId = (Long)row.get("member_coupon_id");
        if (memberCouponId != null) {
            MemberCoupon memberCoupon = getMemberCoupon(row, member, memberCouponId);
            order = new Order(orderId, member, new ArrayList<>(), memberCoupon);
        }
        return order;
    }

    private Member getMember(Map<String, Object> row) {
        Long id = (Long)row.get("member_id");
        String email = (String)row.get("email");
        String password = (String)row.get("password");
        String nickname = (String)row.get("nickname");
        return new Member(id, email, password, nickname);
    }

    private MemberCoupon getMemberCoupon(Map<String, Object> row, Member member, Long memberCouponId) {
        Long couponId = (Long)row.get("coupon_id");
        String couponName = (String)row.get("coupon_name");
        int minOrderPrice = (int)row.get("min_order_price");
        Integer maxDiscountPrice = (Integer)row.get("max_discount_price");
        CouponType type = CouponType.valueOf((String)row.get("type"));
        Integer discountAmount = (Integer)row.get("discount_amount");
        Double discountPercentage = (Double)row.get("discount_percentage");
        Coupon coupon = new Coupon(couponId, couponName, minOrderPrice, type, discountAmount,
            discountPercentage,
            maxDiscountPrice);

        Timestamp expiredAt = (Timestamp)row.get("expired_at");

        return new MemberCoupon(memberCouponId, member, coupon, expiredAt);
    }

    private void addOrderItems(List<Map<String, Object>> rows, Map<Long, Order> orderMap) {
        for (Map<String, Object> row : rows) {
            Long orderId = (Long)row.get("order_id");
            Order order = orderMap.get(orderId);

            Long itemId = (Long)row.get("item_id");
            Long productId = (Long)row.get("product_id");
            String name = (String)row.get("name");
            int price = (int)row.get("price");
            String imageUrl = (String)row.get("image_url");
            int quantity = (int)row.get("quantity");

            OrderItem orderItem = new OrderItem(itemId, orderId, productId, name, price, imageUrl, quantity);
            order.addOrderItem(orderItem);
        }
    }

    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO `order` (member_id, member_coupon_id, shipping_fee, total_price) VALUES (?, ?, ?, ?)",
                new String[] {"ID"});

            ps.setLong(1, order.getMember().getId());
            ps.setLong(2, order.getMemberCoupon().getId());
            ps.setInt(3, order.getShippingFee().value());
            ps.setInt(4, order.getDiscountedPrice().value());

            return ps;
        }, keyHolder);

        List<Map<String, Object>> keys = keyHolder.getKeyList();
        return ((Number)keys.get(0).get("ID")).longValue();
    }

    public Order findWithoutOrderItems(Long orderId) {
        String query = "SELECT * FROM `order` " +
            "JOIN member m ON m.id = member_id " +
            "LEFT JOIN member_coupon mc ON mc.id = member_coupon_id " +
            "LEFT JOIN coupon c ON mc.coupon_id = c.id " +
            "WHERE o.id = ?";

        return jdbcTemplate.queryForObject(query,
            (rs, rn) -> {
                Long id = rs.getLong("m.id");
                String email = rs.getString("m.email");
                String password = rs.getString("m.password");
                String nickname = rs.getString("m.nickname");

                Member member = new Member(id, email, password, nickname);

                Long memberCouponId = rs.getLong("mc.id");
                String couponName = rs.getString("c.name");
                int minOrderPrice = rs.getInt("c.min_order_price");
                Integer maxDiscountPrice = rs.getInt("c.max_discount_price");
                CouponType type = CouponType.valueOf(rs.getString("c.type"));
                Integer discountAmount = rs.getInt("c.discount_amount");
                Double discountPercentage = rs.getDouble("c.discount_percentage");
                Coupon coupon = new Coupon(memberCouponId, couponName, minOrderPrice, type, discountAmount,
                    discountPercentage,
                    maxDiscountPrice);

                Timestamp expiredAt = rs.getTimestamp("expired_at");
                MemberCoupon memberCoupon = new MemberCoupon(memberCouponId, member, coupon, expiredAt);

                return new Order(orderId, member, new ArrayList<>(), memberCoupon);
            },
            orderId);
    }

    public void validate(Long memberId, Long orderId) {
        String query = "SELECT member_id FROM `order` WHERE id = ?";
        Long id = jdbcTemplate.queryForObject(query, Long.class, orderId);

        if (id == null) {
            throw new BadRequestException(ExceptionType.ORDER_NO_EXIST);
        }

        if (!id.equals(memberId)) {
            throw new ForbiddenException(ExceptionType.FORBIDDEN);
        }
    }

    public void delete(Long orderId) {
        String query = "DELETE FROM `order` WHERE id = ?";
        jdbcTemplate.update(query, orderId);
    }
}
