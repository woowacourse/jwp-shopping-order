package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemDto;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

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

    @DisplayName("장바구니에 있는 상품을 주문하면 주문의 ID를 반환한다.")
    @Test
    void shouldReturnOrderIdWhenOrderWithCartItems() {
        OrderRequest orderRequest = new OrderRequest(
                List.of(CartItemDto.of(new CartItem(1L, 1, product1, member1))),
                List.of(1L),
                3_000
        );

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.body().jsonPath().getString(".")).contains("orderId", "1")
        );
    }

    @DisplayName("사용자의 모든 주문을 조회한다.")
    @Test
    void shouldReturnAllOrdersWhenRequestByMember() {
        OrderRequest orderRequest = new OrderRequest(
                List.of(CartItemDto.of(new CartItem(1L, 1, product1, member1))),
                List.of(1L),
                3_000
        );
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getString(".")).contains("originalPrice", "10000");
    }

    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
