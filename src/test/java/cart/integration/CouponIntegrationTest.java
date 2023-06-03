package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.repository.MemberRepository;
import io.restassured.response.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberRepository.findById(1L).get();
    }

    @DisplayName("사용자가 사용 가능한 쿠폰 목록을 조회한다.")
    @Test
    void showCoupons() {
        // given, when
        final ExtractableResponse<Response> response = 쿠폰_목록_조회(member);

        // then
        final List<CouponResponse> couponResponses = response.jsonPath().getList(".", CouponResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(couponResponses)
                .containsOnly(
                        new CouponResponse(1L, "배송비 3000원 할인", 3000),
                        new CouponResponse(2L, "신규 가입 할인", 5000),
                        new CouponResponse(3L, "여름 맞이 할인", 2000));
    }

    @DisplayName("존재하지 않는 사용자 정보로 쿠폰 목록을 조회하면 실패한다.")
    @Test
    void showCouponsNotExistingMember() {
        // given, when
        final ExtractableResponse<Response> response = 쿠폰_목록_조회(new Member(100L, "no@gmail.com", "1234"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> 쿠폰_목록_조회(final Member member) {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/coupons")
                .then()
                .log().all()
                .extract();
    }
}
