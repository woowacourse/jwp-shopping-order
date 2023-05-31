package cart.repository;

import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderCouponDao 은(는)")
@JdbcTest
class OrderCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderCouponDao orderCouponDao;

    @BeforeEach
    void setUp() {
        orderCouponDao = new OrderCouponDao(jdbcTemplate);
    }

    @Test
    void 주문_쿠폰을_저장한다() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L, MEMBER.getId());
        OrderItemEntity orderItemEntity = getOrderItemEntity(1L, orderEntity.getId(), CHICKEN, 10);
        Coupon coupon = new Coupon(1L, "10% 쿠폰", CouponType.RATE, 10);
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        insertCoupon(coupon);
        insertOrder(orderEntity);
        insertOrderItem(orderItemEntity);
        OrderCouponEntity orderCouponEntity = new OrderCouponEntity(orderItemEntity.getId(), coupon.getId());

        // when
        Long actual = orderCouponDao.save(orderCouponEntity);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 주문_아이디로_모든_쿠폰을_찾는다() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L, MEMBER.getId());
        OrderItemEntity orderItemChicken = getOrderItemEntity(1L, orderEntity.getId(), CHICKEN, 10);
        OrderItemEntity orderItemPizza = getOrderItemEntity(2L, orderEntity.getId(), PIZZA, 5);
        Coupon coupon = new Coupon(1L, "10% 쿠폰", CouponType.RATE, 10);
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        insertProduct(PIZZA);
        insertCoupon(coupon);
        insertOrder(orderEntity);
        insertOrderItem(orderItemChicken);
        insertOrderItem(orderItemPizza);
        OrderCouponEntity orderChickenCoupon = new OrderCouponEntity(orderItemChicken.getId(), coupon.getId());
        OrderCouponEntity orderPizzaCoupon = new OrderCouponEntity(orderItemChicken.getId(), coupon.getId());
        orderCouponDao.save(orderChickenCoupon);
        orderCouponDao.save(orderPizzaCoupon);

        // when
        List<OrderCouponEntity> actual = orderCouponDao.findByOrderId(orderEntity.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);
    }

    private void insertCoupon(Coupon coupon) {
        String sql = "INSERT INTO coupon (id, name, type, discount_amount) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, coupon.getId(), coupon.getName(), coupon.getType().name(), coupon.getDiscountAmount());
    }

    private void insertProduct(Product product) {
        String sql = "INSERT INTO Product (id, name, price, image_url) VALUES (?, ?,?,?)";
        jdbcTemplate.update(sql, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    private void insertMember(Member member) {
        String sql = "INSERT INTO member (id, email, password) VALUES (?, ?,?)";
        jdbcTemplate.update(sql, member.getId(), member.getEmail(), member.getPassword());
    }

    private void insertOrder(OrderEntity orderEntity) {
        String sql = "INSERT INTO orders (id, member_id) VALUES (?,?)";
        jdbcTemplate.update(sql, orderEntity.getId(), orderEntity.getMemberId());
    }

    private void insertOrderItem(OrderItemEntity orderItemEntity) {
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

    private OrderItemEntity getOrderItemEntity(Long id, Long orderId, Product product, int quantity) {
        return new OrderItemEntity(id, orderId, product.getId(), quantity, product.getName(), product.getPrice(),
                product.getImageUrl());
    }
}
