package cart.acceptance;

import cart.dto.response.ProfileResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileAcceptanceTest extends AcceptanceTest {

    @Test
    void 로그인된_사용자의_포인트를_조회한다() {
        final var response = RestAssured.given().log().all()
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .get("/profile")
                .then().log().all()
                .extract();

        final var profileResponse = response.as(ProfileResponse.class);

        assertThat(profileResponse.getPoints()).isEqualTo(1320);
    }
}
