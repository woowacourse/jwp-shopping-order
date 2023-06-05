package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dao.MemberDao;
import cart.domain.Member;

public class MemberIntegrationTest extends IntegrationTest {
    @Autowired
    MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.findById(1L);
    }

    @Test
    @DisplayName("회원의 정보를 조회한다.")
    void profileTest() {
        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/members/profile")
            .then()
            .extract();

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> {
                long id = response.body().jsonPath().getLong("id");
                String email = response.body().jsonPath().getString("email");
                String nickname = response.body().jsonPath().getString("nickname");
                assertAll(
                    () -> assertThat(id).isEqualTo(1L),
                    () -> assertThat(email).isEqualTo("a@a.com"),
                    () -> assertThat(nickname).isEqualTo("라잇")
                );
            }
        );
    }
}
