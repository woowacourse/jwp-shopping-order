package cart.integration;

import cart.ui.advcie.ErrorType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@SuppressWarnings("ALL")
public class AuthIntegrationTest extends IntegrationTest {

    @Test
    void 로그인한다() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("a@a.com", "1234")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    void 예외() {
        Exception ddd = new IllegalArgumentException("dd");
        ErrorType from = ErrorType.from(ddd);
        System.out.println(from);
    }
}
