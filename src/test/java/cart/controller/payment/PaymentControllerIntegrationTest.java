package cart.controller.payment;

import cart.domain.coupon.MemberCoupons;
import cart.domain.member.Member;
import cart.dto.coupon.CouponIdRequest;
import cart.dto.payment.PaymentRequest;
import cart.dto.product.ProductIdRequest;
import cart.repository.member.MemberRepository;
import cart.service.payment.PaymentService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.CouponFixture.createCoupons;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/setData.sql")
public class PaymentControllerIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("결제 페이지를 조회한다.")
    @Test
    void find_payment_page() {
        // given
        Member member = memberRepository.findMemberById(1);
        MemberCoupons memberCoupons = new MemberCoupons(member, createCoupons());
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));
        paymentService.pay(memberCoupons, req);

        // when & then
        Response response = given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when().get("payments");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("products[0].productId", equalTo(1))
                .body("products[0].productName", equalTo("치킨"))
                .body("products[0].price", equalTo(10000))
                .body("products[0].quantity", equalTo(9))
                .body("products[0].imgUrl", equalTo("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"))
                .body("products[0].isOnSale", equalTo(false))
                .body("products[0].salePrice", equalTo(0))
                .body("coupons[0].couponId", equalTo(2))
                .body("coupons[0].couponName", equalTo("전체 2000원 할인 쿠폰"));
    }

    @DisplayName("쿠폰 적용을 한다.")
    @Test
    void apply_coupon() {
        // given
        // when & then
        Response response = given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/payments/coupons?couponsIds=1");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("products[0].productId", equalTo(1))
                .body("products[0].originalPrice", equalTo(10000))
                .body("products[0].discountPrice", equalTo(1000))
                .body("deliveryPrice.originalPrice", equalTo(3000))
                .body("deliveryPrice.discountPrice", equalTo(0));
    }

    @DisplayName("주문을 한다.")
    @Test
    void pay() {
        // given
        PaymentRequest req = new PaymentRequest(List.of(new ProductIdRequest(1L, 1)), List.of(new CouponIdRequest(1L)));

        // when & then
        Response response = given()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .contentType(ContentType.JSON)
                .body(req)
                .post("/payments");

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/payments/1");
    }
}
