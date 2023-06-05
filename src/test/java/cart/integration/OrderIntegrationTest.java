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
import cart.domain.CartItem;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.domain.MemberCoupon;
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

    private CartItem cartItem1;
    private CartItem cartItem2;
    private Long couponId;
    private Long memberCouponId;
    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.findById(1L);
        cartItem1 = cartItemDao.findById(1L);
        cartItem2 = cartItemDao.findById(2L);
        Coupon coupon = new Coupon(
            null,
            "할인쿠폰",
            100,
            CouponType.FIXED_PERCENTAGE,
            null,
            0.1,
            10000);
        couponId = couponDao.save(coupon);
        Coupon newCoupon = new Coupon(couponId, "할인쿠폰",
            100,
            CouponType.FIXED_PERCENTAGE,
            null,
            0.1,
            10000);
        MemberCoupon memberCoupon = new MemberCoupon(null, member, newCoupon, Timestamp.valueOf(LocalDateTime.MAX));
        memberCouponId = memberCouponDao.save(memberCoupon);
    }

    @DisplayName("주문을 한다.")
    @Test
    void addOrder() {
        OrderRequest orderRequest = new OrderRequest(List.of(
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
}
