package cart.ui.cartitem;

import cart.application.repository.MemberRepository;
import cart.fixture.MemberFixture;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/reset.sql")
class CartItemReadControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("[GET] /cart-items 사용자에 맞는 장바구니 정보를 반환한다.(사용자가 없을때 예외처리)")
    void showCartItemsNotExistMember() {

        String email = "leo@gmail.com";
        String password = "leo123";

        String base64Credentials = java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/cart-items")
                .then().log().all()
                .extract();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("[GET] /cart-items 사용자에 맞는 장바구니 정보를 반환한다.")
    void showCartItems() {
        memberRepository.createMember(MemberFixture.레오);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(MemberFixture.레오.getEmail(), MemberFixture.레오.getPassword())
                .when().get("/cart-items")
                .then().log().all()
                .extract();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
