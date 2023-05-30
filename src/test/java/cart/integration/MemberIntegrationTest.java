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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        this.member = memberDao.getMemberById(1L);
        super.setUp();
    }

    @DisplayName("사용자의 포인트를 조회한다.")
    @Test
    void checkMemberPoint() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/members/points")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.path("point").equals(0)).isTrue()
        );
    }
}
