package cart.integration;

import cart.db.repository.MemberRepository;
import cart.domain.member.Member;
import cart.dto.coupon.MemberCouponResponse;
import cart.dto.login.MemberRequest;
import cart.exception.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.exception.ErrorCode.ALREADY_ISSUED_COUPON;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberCouponIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member1;

    @BeforeEach
    void setUp() {
        super.setUp();

        회원가입_요청(new MemberRequest("member1", "1234"));
        member1 = memberRepository.findById(1L);
    }

    @DisplayName("사용자에게 쿠폰을 발급한다.")
    @Test
    void addMemberCoupon() {
        // when
        ExtractableResponse<Response> response = 쿠폰_발급_요청(member1, 2L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("이미 발급받은 쿠폰을 발급 요청 시, ALREADY_ISSUED_COUPON 예외가 발생한다.")
    @Test
    void addMemberCoupon_AlreadyIssued() {
        // given
        쿠폰_발급_요청(member1, 2L);

        // when
        ExtractableResponse<Response> response = 쿠폰_발급_요청(member1, 2L);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(ALREADY_ISSUED_COUPON)
        );
    }

    @DisplayName("사용자가 발급받은 쿠폰을 모두 조회한다.")
    @Test
    void getMemberCoupons() {
        // given
        쿠폰_발급_요청(member1, 2L);

        // when
        ExtractableResponse<Response> response = 사용자_쿠폰_조회(member1);
        List<MemberCouponResponse> couponResponses = response.body()
                .jsonPath()
                .getList(".", MemberCouponResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(couponResponses.size()).isEqualTo(2),
                () -> assertThat(couponResponses.get(0).getName()).isEqualTo("신규 가입 할인 쿠폰"),
                () -> assertThat(couponResponses.get(1).getName()).isEqualTo("테스트 쿠폰")
        );
    }

    private void 회원가입_요청(final MemberRequest memberRequest) {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/users/join")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> 쿠폰_발급_요청(final Member member, final Long couponId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .post("/users/me/coupons/{couponId}", couponId)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 사용자_쿠폰_조회(final Member member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getName(), member.getPassword())
                .when()
                .get("/users/me/coupons")
                .then()
                .extract();
    }
}
