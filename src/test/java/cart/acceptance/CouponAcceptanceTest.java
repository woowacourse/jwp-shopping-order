package cart.acceptance;

import cart.dao.MemberDao;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponReissueRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;

@SuppressWarnings("NonAsciiCharacters")
@Import(MemberDao.class)
public class CouponAcceptanceTest extends IntegrationTest {

    /**
     * given 쿠폰 ID와 사용자 정보를 가지고
     * when 발급 요청을 하면
     * then 성공한다
     */
    @Test
    void 쿠폰을_사용자에게_발급해준다() {
        // given
        final CouponIssueRequest request = new CouponIssueRequest(1L);

        // when
        final ExtractableResponse<Response> 결과 = 쿠폰을_발급한다(request);

        // then
        assertAll(
                () -> assertThat(결과.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(결과.header(HttpHeaders.LOCATION)).contains("/coupons/")
        );
    }

    private ExtractableResponse<Response> 쿠폰을_발급한다(CouponIssueRequest request) {
        return givenBasic()
                .body(request)
                .when()
                .post("/coupons")
                .then().log().all()
                .extract();
    }

    /**
     * given 쿠폰 아이디와 회원 정보를 주어지면
     * when 사용한 쿠폰을 재발급 요청하면
     * then 쿠폰이 재발급 된다.
     */
    @Test
    void 사용한_쿠폰을_재발급_한다() {
        // given
        final CouponReissueRequest request = new CouponReissueRequest(1L, "a@a.com", "1234");

        // when
        final ExtractableResponse<Response> response = 쿠폰을_재발급_한다(request, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 쿠폰을_재발급_한다(final CouponReissueRequest request, final long couponId) {
        return givenBasic()
                .body(request)
                .patch("/coupons/{couponId}", couponId)
                .then().log().all()
                .extract();
    }

    private RequestSpecification givenBasic() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234");
    }
}
