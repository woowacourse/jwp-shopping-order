package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
class UserIntegrationTest extends IntegrationTest {

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = new Member(1L, "odo1@woowa.com", "1234");
    }

    @DisplayName("유저 정보를 조회한다.")
    @Test
    void getUser() {
        final ExtractableResponse<Response> response = requestGetUser(member);
        final JsonPath jsonPath = response.jsonPath();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getString("email")).isEqualTo("odo1@woowa.com"),
                () -> assertThat(jsonPath.getInt("point")).isEqualTo(600),
                () -> assertThat(jsonPath.getDouble("earnRate")).isEqualTo(5)
        );
    }

    private ExtractableResponse<Response> requestGetUser(final Member member) {
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/users")
                .then()
                .log().all()
                .extract();
    }
}
