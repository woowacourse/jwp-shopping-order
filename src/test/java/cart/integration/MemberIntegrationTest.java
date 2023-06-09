package cart.integration;

import cart.dto.login.MemberRequest;
import cart.exception.ErrorResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static cart.exception.ErrorCode.DUPLICATED_NAME;
import static cart.exception.ErrorCode.NOT_AUTHENTICATION_MEMBER;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberIntegrationTest extends IntegrationTest {

    private MemberRequest member1;
    private MemberRequest member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        member1 = new MemberRequest("member1", "1234");
        member2 = new MemberRequest("member2", "1234");


    }

    @DisplayName("회원가입에 성공한다.")
    @Test
    void join() {
        // when
        ExtractableResponse<Response> response = 회원가입_요청(member1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("중복되는 아이디로 회원가입 시, DUPLICATED_NAME 예외가 발생한다.")
    @Test
    void joinFail_WithDuplicatedName() {
        // when
        회원가입_요청(member1);
        ExtractableResponse<Response> response = 회원가입_요청(member1);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(DUPLICATED_NAME)
        );
    }

    @DisplayName("회원가입한 계정으로 로그인한다.")
    @Test
    void login() {
        // given
        회원가입_요청(member1);

        // when
        ExtractableResponse<Response> response = 로그인_요청(member1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 계정으로 로그인 시, NOT_AUTHENTICATION_MEMBER 예외가 발생한다.")
    @Test
    void loginFail_WithInvalidAccount() {
        // given
        회원가입_요청(member1);

        // when
        ExtractableResponse<Response> response = 로그인_요청(member2);
        ErrorResponse errorResponse = response.body()
                .jsonPath()
                .getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(NOT_AUTHENTICATION_MEMBER)
        );
    }

    private ExtractableResponse<Response> 회원가입_요청(final MemberRequest member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .when()
                .post("/users/join")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_요청(final MemberRequest member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .when()
                .post("/users/login")
                .then()
                .extract();
    }
}
