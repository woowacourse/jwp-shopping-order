package cart.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CommonSteps {

    public static HttpStatus 정상_요청 = HttpStatus.OK;
    public static HttpStatus 정상_생성 = HttpStatus.CREATED;
    public static HttpStatus 정상_요청이지만_반환값_없음 = HttpStatus.NO_CONTENT;
    public static HttpStatus 비정상_요청 = HttpStatus.BAD_REQUEST;

    public static final HttpMethod 조회 = HttpMethod.GET;
    public static final HttpMethod 추가 = HttpMethod.POST;
    public static final HttpMethod 수정 = HttpMethod.PATCH;
    public static final HttpMethod 제거 = HttpMethod.DELETE;


    public static void 요청_결과의_상태를_검증한다(final ExtractableResponse<Response> 요청_결과, final HttpStatus 상태) {
        assertThat(요청_결과.statusCode()).isEqualTo(상태.value());
    }

    public static void 요청_결과가_반환하는_리소스의_위치가_존재하는지_확인한다(final ExtractableResponse<Response> 요청_결과) {
        assertThat(요청_결과.header("Location")).isNotBlank();
    }
}
