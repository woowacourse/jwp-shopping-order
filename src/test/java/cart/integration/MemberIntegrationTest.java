package cart.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import cart.application.dto.MemberSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:/init.sql")
public class MemberIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("사용자를 추가한다.")
    void join() {
        // given
        final MemberSaveRequest 져니_저장_요청 = new MemberSaveRequest("journey", "password");

        // expected
        given()
            .when()
            .body(져니_저장_요청)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/users/join")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", "/users/" + 1);
    }

    @Test
    @DisplayName("사용자 정보를 조회한다.")
    void getMember() {
        // given
        final MemberSaveRequest 져니_저장_요청 = new MemberSaveRequest("journey", "password");
        사용자_저장(져니_저장_요청);

        // expected
        given()
            .when()
            .get("/users/{id}", 1)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("name", equalTo("journey"))
            .body("password", equalTo("password"));
    }

    @Test
    @DisplayName("전체 사용자 정보를 조회한다.")
    void getMembers() {
        // given
        final MemberSaveRequest 져니_저장_요청 = new MemberSaveRequest("journey", "jourzura1");
        final MemberSaveRequest 라온_저장_요청 = new MemberSaveRequest("raon", "jourzura2");
        final MemberSaveRequest 쥬니_저장_요청 = new MemberSaveRequest("zuny", "jourzura3");
        사용자_저장(져니_저장_요청);
        사용자_저장(라온_저장_요청);
        사용자_저장(쥬니_저장_요청);

        // expected
        given()
            .when()
            .get("/users")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("size", is(3))
            .body("[0].id", equalTo(1))
            .body("[0].name", equalTo("journey"))
            .body("[0].password", equalTo("jourzura1"))
            .body("[1].id", equalTo(2))
            .body("[1].name", equalTo("raon"))
            .body("[1].password", equalTo("jourzura2"))
            .body("[2].id", equalTo(3))
            .body("[2].name", equalTo("zuny"))
            .body("[2].password", equalTo("jourzura3"));
    }

    private void 사용자_저장(final MemberSaveRequest 사용자_저장_요청) {
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(사용자_저장_요청)
            .when()
            .post("/users/join")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }
}
