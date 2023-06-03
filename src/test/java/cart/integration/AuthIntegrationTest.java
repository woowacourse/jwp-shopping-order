package cart.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Base64;

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
}
