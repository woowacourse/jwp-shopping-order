package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.application.dto.auth.LoginResponse;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class AuthIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void 로그인_성공() {
        String email = "test@test.com";
        String password = "test!";
        String nickname = "testNick";
        long memberId = memberDao.create(new MemberEntity(email, password, nickname));

        LoginResponse response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .when()
                .post("/auth/login")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(LoginResponse.class);

        LoginResponse expected = new LoginResponse(memberId, email, nickname);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
