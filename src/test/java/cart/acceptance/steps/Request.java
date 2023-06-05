package cart.acceptance.steps;

import cart.domain.member.Member;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpMethod;

public class Request {

    private final ExtractableResponse<Response> response;

    public Request(final ExtractableResponse<Response> response) {
        this.response = response;
    }

    public ExtractableResponse<Response> 요청_결과_반환() {
        return response;
    }

    public static class Builder {

        private RequestSpecification requestSpecification;

        public Builder(final RequestSpecification requestSpecification) {
            this.requestSpecification = requestSpecification;
        }

        public static Builder 요청_생성() {
            final RequestSpecification request = RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON);
            return new Builder(request);
        }

        public static Builder 사용자의_요청_생성(final Member 사용자) {
            final RequestSpecification request = RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON)
                    .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword());
            return new Builder(request);
        }

        public <T> Builder 전송_정보(final T 입력값) {
            this.requestSpecification = requestSpecification.body(입력값);
            return this;
        }

        public Request 요청_위치(final HttpMethod httpMethod, final String uri) {
            Response response = null;
            if (httpMethod == HttpMethod.GET) {
                response = requestSpecification.when().get(uri);
            }
            if (httpMethod == HttpMethod.POST) {
                response = requestSpecification.when().post(uri);
            }
            if (httpMethod == HttpMethod.PATCH) {
                response = requestSpecification.when().patch(uri);
            }
            if (httpMethod == HttpMethod.PUT) {
                response = requestSpecification.when().put(uri);
            }
            if (httpMethod == HttpMethod.DELETE) {
                response = requestSpecification.when().delete(uri);
            }

            return new Request(
                    response.then().log()
                            .all().extract()
            );
        }
    }
}
