package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("멤버 관련 기능")
class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        super.setUp();

        email = "test@example.com";
        password = "1234";
        memberDao.addMember(new Member(null, email, password));
    }

    @DisplayName("주문을 위한 사용자의 정보를 가져온다.")
    @Test
    void showMemberInfoForOrder() {
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .log().all()
                .when()
                .get("/users")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("로그인에 실패하면 요청이 실패한다.")
    @Test
    void showMemberInfoFailLogin() {
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(".", ".")
                .log().all()
                .when()
                .get("/users")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
