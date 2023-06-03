package cart.ui.cartitem;

import cart.application.repository.MemberRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static cart.fixture.MemberFixture.레오;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:/reset.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
        Response response = RestAssured.given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .when().get("/cart-items")
                .then().log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("[GET] /cart-items 사용자에 맞는 장바구니 정보를 반환한다.")
    void showCartItems() {
        memberRepository.createMember(레오);
        Response response = RestAssured.given().log().all()
                .auth().preemptive().basic(레오.getEmail(), 레오.getPassword())
                .when().get("/cart-items")
                .then().log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
