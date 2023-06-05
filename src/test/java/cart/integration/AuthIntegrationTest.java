package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dao.MemberDao;
import cart.domain.Member;

public class AuthIntegrationTest extends IntegrationTest{

    @Autowired
    MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.findById(1L);
    }

    @Test
    @DisplayName("로그인을 한다.")
    void logInTest() {
        var response = given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .post("/auth/login")
            .then()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
