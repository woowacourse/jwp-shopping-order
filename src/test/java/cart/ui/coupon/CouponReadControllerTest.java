package cart.ui.coupon;

import cart.application.repository.CouponRepository;
import cart.application.repository.MemberRepository;
import cart.domain.Member;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static cart.fixture.MemberFixture.레오;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/reset.sql")
class CouponReadControllerTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        memberRepository.createMember(new Member("leo", 레오.getEmail(), 레오.getPassword()));
    }

    @Test
    void findCoupons() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .when().get("/coupons")
                .then().log().all()
                .extract();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
