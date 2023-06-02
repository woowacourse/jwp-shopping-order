package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MemberIntegrationTest extends IntegrationTest {

    @DisplayName("초기 멤버를 생성하면 기본 포인트는 5,000원이다.")
    @Test
    void member_initial_point() {
        Member member = memberTestSupport.builder().build();

        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("members/points")
                .then()
                .extract();
        int point = response.jsonPath().getInt("point");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(point).isEqualTo(5_000)
        );
    }
}
