package cart.integration;

import cart.TestFixture;
import cart.domain.member.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberIntegrationTest extends IntegrationTest {

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        this.member = TestFixture.getMember1();
    }

    @DisplayName("사용자의 포인트를 조회한다.")
    @Test
    void checkMemberPoint() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/members/points")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.path("point").equals(0)).isTrue()
        );
    }
}
