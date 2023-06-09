package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.application.dto.member.FindProfileResponse;
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
public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void 본인_프로필_조회() {
        String email = "test@test.com";
        String password = "test!";
        String nickname = "testNick";
        long memberId = memberDao.create(new MemberEntity(email, password, nickname));

        FindProfileResponse response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .when()
                .get("/members/profile")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(FindProfileResponse.class);

        FindProfileResponse expected = new FindProfileResponse(memberId, email, nickname);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
