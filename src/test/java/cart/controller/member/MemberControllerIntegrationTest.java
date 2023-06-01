package cart.controller.member;

import cart.dto.member.MemberCreateRequest;
import cart.service.member.MemberService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class MemberControllerIntegrationTest {

    @Autowired
    private MemberService memberService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("멤버를 생성한다.")
    @Test
    void create_member() {
        // given
        MemberCreateRequest req = new MemberCreateRequest("c@c.com", "1234");

        // when
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/members");

        // then
        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("멤버를 전체 조회한다.")
    @Test
    void find_all_members() {
        // when & then
        Response response = given()
                .when().get("/members");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].memberId", equalTo(1))
                .body("[0].email", equalTo("a@a.com"))
                .body("[0].password", equalTo("1234"));
    }

    @DisplayName("멤버를 조회한다.")
    @Test
    void find_member() {
        // when & then
        Response response = given()
                .when().get("/members/1");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("memberId", equalTo(1))
                .body("email", equalTo("a@a.com"))
                .body("password", equalTo("1234"));
    }

    @DisplayName("멤버를 삭제한다.")
    @Test
    void delete_member() {
        // when & then
        Response response = given()
                .when().delete("/members/1");

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
