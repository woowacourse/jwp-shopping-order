package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class PointIntegrationTest extends IntegrationTest {

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        this.member = new Member("a@a.com", "1234", new Point(5_000));
    }

    @Test
    void 저장된_회원의_보유_포인트를_조회한다() {
        // given
        memberRepository.save(member);

        // when
        var response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .extract();

        // then
        assertThat(response.jsonPath().getObject("userPoint", Integer.class)).isEqualTo(5_000);
    }

    @Test
    void 존재하지_않는_이메일이_포함된_요청으로_포인트를_조회하면_실패한다() {
        // when
        var response = given()
                .auth().preemptive().basic(member.getEmail() + "asdf", member.getPassword())
                .when()
                .get("/points")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 잘못된_회원_정보로_포인트를_조회하면_실패한다() {
        // when
        var response = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword() + "asdf")
                .when()
                .get("/points")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
