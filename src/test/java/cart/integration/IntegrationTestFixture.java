package cart.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTestFixture {

    public static void 상태_코드를_검증한다(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static Long 아이디를_반환한다(ExtractableResponse<Response> 응답) {
        return Long.parseLong(응답.header(HttpHeaders.LOCATION).split("/")[2]);
    }
}
