package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CouponCreateRequest;
import cart.dto.CouponIssueRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Product product1;
    private Product product2;
    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();
        product1 = new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg");
        product2 = new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg");
        member1 = new Member(1L, "a@a.com", "1234");
        member2 = new Member(2L, "b@b.com", "1234");
    }

    @DisplayName("사용자에게 쿠폰을 발급하면 Status OK를 응답한다.")
    @Test
    void shouldResponseOkWhenIssueCouponToMember() {
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(1L);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(couponIssueRequest)
                .when()
                .post("/coupons/me")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("모든 쿠폰을 조회한다.")
    @Test
    void shouldReturnAllCouponsWhenRequest() {
        ExtractableResponse<Response> response = given().log().all()
                .get("/coupons")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getString(".")).contains("name", "퍼센트 할인 쿠폰", "가격 쿠폰")
        );
    }

    @DisplayName("사용자가 보유한 쿠폰을 조회한다.")
    @Test
    void shouldReturnCouponsOfMemberWhenRequest() {
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(1L);

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(couponIssueRequest)
                .when()
                .post("/coupons/me")
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response = given().log().all()
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .when()
                .get("/coupons/me")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getString(".")).contains("name", "퍼센트 할인 쿠폰")
        );
    }

    @DisplayName("쿠폰을 생성한다")
    @Test
    void shouldCreateCouponWhenRequest() {
        CouponCreateRequest couponCreateRequest = new CouponCreateRequest("percent", 10, "신규 회원 환영 쿠폰");

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(couponCreateRequest)
                .when()
                .post("/coupons")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.body().jsonPath().getString(".")).contains("name", "신규 회원 환영 쿠폰")
        );
    }
}
