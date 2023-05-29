package cart.acceptance.common;

import static org.assertj.core.api.Assertions.assertThat;

import cart.member.domain.Member;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CommonAcceptanceSteps {

    public static final HttpStatus 정상_요청 = HttpStatus.OK;
    public static final HttpStatus 정상_생성 = HttpStatus.CREATED;
    public static final HttpStatus 본문없음 = HttpStatus.NO_CONTENT;
    public static final HttpStatus 인증되지않음 = HttpStatus.UNAUTHORIZED;
    public static final HttpStatus 권한없음 = HttpStatus.FORBIDDEN;

    public static RequestSpecification given() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON);
    }

    public static RequestSpecification given(
            Member 회원
    ) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword());
    }

    public static void 응답을_검증한다(
            ExtractableResponse<Response> 응답,
            HttpStatus 상태코드
    ) {
        assertThat(응답.statusCode()).isEqualTo(상태코드.value());
    }

    public static Long 생성된_ID(
            ExtractableResponse<Response> 응답
    ) {
        String[] split = 응답.header("location").split("/");
        return Long.parseLong(split[split.length - 1]);
    }
}
