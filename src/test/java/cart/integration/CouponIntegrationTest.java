package cart.integration;

import cart.dto.CouponIssueRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;

@SuppressWarnings("NonAsciiCharacters")
public class CouponIntegrationTest extends IntegrationTest {

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

    private static RequestSpecification givenBasic() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234");
    }
}
