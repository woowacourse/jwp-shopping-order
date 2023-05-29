package cart.integration;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.dto.OrderRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;
    private final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1000);

    @DisplayName("장바구니에 들어 있는 상품을 주문할 수 있다.")
    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.getMemberById(1L);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .body(orderRequest).log().all()
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/1");
    }

    @DisplayName("주문 id와 연관된 ProductItem 정보를 확인할 수 있다.")
    @Test
    void orderAndGetOrderDetail() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .when().get("/orders/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
    @DisplayName("단일 주문에 대한 주문 상품들을 볼 수 있다.")
    @Test
    void getAllOrderDetails() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .when().get("/orders/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("orderId", is(1))
                .body("totalPrice", is(100_000))
                .body("usedPoint", is(1_000))
                .body("products", hasSize(2))
                .body("products[0].productId", is(1))
                .body("products[1].productId", is(2));
    }
}
