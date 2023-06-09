package cart.integration;

import cart.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.fixtures.CouponFixtures.*;
import static cart.fixtures.MemberFixtures.MEMBER1;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자의 쿠폰 목록을 가져온다.")
    void findMemberCouponsTest() throws JsonProcessingException {
        ExtractableResponse<Response> response = requestGetCoupons(MEMBER1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(List.of(COUPON1_RESPONSE, COUPON3_RESPONSE)));
    }

    private ExtractableResponse<Response> requestGetCoupons(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/coupons")
                .then()
                .log().all()
                .extract();
    }
}
