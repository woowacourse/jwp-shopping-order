package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.DepositRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @Test
    void 특정_유저의_포인트를_충전하다() {
        final DepositRequest request = DepositRequest.from(5000L);

        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/members/cash")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_유저의_포인트를_확인하다() {
        final ExtractableResponse<Response> response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/cash")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
