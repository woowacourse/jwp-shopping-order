package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.MemberCouponAddRequest;
import cart.dto.response.CouponResponse;
import cart.dto.response.MemberCouponResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CouponIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        member1 = memberDao.findById(1L);
        member2 = memberDao.findById(2L);
    }

    @Test
    @DisplayName("선택한 장바구니에 대하여 회원의 쿠폰 내역을 조회한다.")
    void getCouponTest() {
        ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
            .when()
            .get("/orders/coupons?cartItemId=1&cartItemId=2")
            .then()
            .log().all()
            .extract();

        List<MemberCouponResponse> coupons = response.body().jsonPath().getList("coupons", MemberCouponResponse.class);
        assertAll(
            () -> assertThat(coupons.get(0).getId()).isEqualTo(1L),
            () -> assertThat(coupons.get(0).getName()).isEqualTo("10프로 할인"),
            () -> assertThat(coupons.get(1).getId()).isEqualTo(2L),
            () -> assertThat(coupons.get(1).getName()).isEqualTo("5000원 할인")
        );
    }

    @Test
    @DisplayName("발급 가능한 쿠폰을 조회한다.")
    void getCouponsTest() {
        ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
            .when()
            .get("/coupons")
            .then()
            .log().all()
            .extract();

        List<CouponResponse> coupons = response.body().jsonPath().getList("coupons", CouponResponse.class);
        assertAll(
            () -> assertThat(coupons.get(0).getId()).isEqualTo(1L),
            () -> assertThat(coupons.get(0).getName()).isEqualTo("10프로 할인"),
            () -> assertThat(coupons.get(1).getId()).isEqualTo(2L),
            () -> assertThat(coupons.get(1).getName()).isEqualTo("5000원 할인"),
            () -> assertThat(coupons.get(2).getId()).isEqualTo(3L),
            () -> assertThat(coupons.get(2).getName()).isEqualTo("20프로 할인"),
            () -> assertThat(coupons.get(3).getId()).isEqualTo(4L),
            () -> assertThat(coupons.get(3).getName()).isEqualTo("10000원 할인")
        );
    }

    @Test
    @DisplayName("회원이 쿠폰을 발급한다.")
    void addCouponTest() {
        ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
            .when()
            .body(new MemberCouponAddRequest(Timestamp.valueOf("2023-06-05 00:00:00").toLocalDateTime()))
            .post("/coupons/3")
            .then()
            .log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response1 = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
            .when()
            .get("/orders/coupons?cartItemId=1&cartItemId=2")
            .then()
            .log().all()
            .extract();

        List<MemberCouponResponse> coupons = response1.body().jsonPath().getList("coupons", MemberCouponResponse.class);

        assertThat(coupons.stream().anyMatch(memberCouponResponse -> memberCouponResponse.getName().equals("20프로 할인"))).isTrue();
    }
}
