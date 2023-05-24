package cart.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CorsTest extends IntegrationTest{

    @Test
    void a() {
        RestAssured.given().log().all()
                .header("Origin", "https://solo5star.github.io/react-shopping-cart-prod")
                .header("Access-Control-Request-Method", "GET")
                .when()
                .get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

    }
}
