package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private MemberCouponDao memberCouponDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    private Member member;
    private OrderRequest orderRequest;
    private Long orderId;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.findById(1L);

        CartItem cartItem1 = cartItemDao.findById(1L);
        CartItem cartItem2 = cartItemDao.findById(2L);
        Coupon coupon = new Coupon(
            null,
            "할인쿠폰",
            100,
            CouponType.FIXED_PERCENTAGE,
            null,
            0.1,
            10000
        );
        Long couponId = couponDao.save(coupon);
        Coupon newCoupon = new Coupon(
            couponId,
            "할인쿠폰",
            100,
            CouponType.FIXED_PERCENTAGE,
            null,
            0.1,
            10000
        );

        MemberCoupon memberCoupon = new MemberCoupon(null, member, newCoupon, Timestamp.valueOf(LocalDateTime.MAX));
        Long memberCouponId = memberCouponDao.save(memberCoupon);
        MemberCoupon newMemberCoupon = new MemberCoupon(memberCouponId, member, newCoupon,
            Timestamp.valueOf(LocalDateTime.MAX));

        List<OrderItem> orderItems = List.of(new OrderItem(null, null, 1L, "지구", 1000, "www.woowa-reo.store", 4));
        Order order = new Order(member, orderItems, newMemberCoupon);
        orderId = orderDao.save(order);
        orderItemDao.saveAllOfOrder(orderId, order);

        orderRequest = new OrderRequest(List.of(
            new CartItemRequest(
                cartItem1.getId(),
                cartItem1.getQuantity(),
                cartItem1.getProduct().getName(),
                cartItem1.getProduct().getPrice(),
                cartItem1.getProduct().getImageUrl()
            ),
            new CartItemRequest(
                cartItem2.getId(),
                cartItem2.getQuantity(),
                cartItem2.getProduct().getName(),
                cartItem2.getProduct().getPrice(),
                cartItem2.getProduct().getImageUrl()
            )
        ), memberCouponId);
    }

    @Test
    @DisplayName("회원의 모든 주문 내역을 조회한다.")
    void getOrders() {
        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders")
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 주문을 조회한다.")
    void getOrderTest() {
        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .body(orderRequest)
            .when()
            .post("/orders")
            .then()
            .extract();

        var response2 = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/orders/" + orderId)
            .then()
            .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

        long id = response2.body().jsonPath().getLong("id");
        assertThat(id).isEqualTo(1L);
    }
    @Test
    @DisplayName("주문을 한다.")
    void addOrder() {
        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .body(orderRequest)
            .when()
            .post("/orders")
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("주문을 삭제한다.")
    void deleteOrder() {
        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .delete("/orders/" + orderId)
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
