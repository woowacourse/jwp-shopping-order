package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CouponIssueRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Product product1;
    private Product product2;
    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();
        product1 = new Product(1L, "치킨", 10_000, "http://example.com/chicken.jpg");
        product2 = new Product(2L, "피자", 15_000, "http://example.com/pizza.jpg");
        member1 = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    @DisplayName("사용자에게 쿠폰을 발급하면 Status OK를 응답한다.")
    @Test
    void shouldResponseOkWhenIssueCouponToMember() {
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(1L);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(couponIssueRequest)
                .when()
                .post("/coupons")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
