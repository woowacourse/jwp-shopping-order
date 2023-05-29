package cart.ui;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemRequest;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OrderApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("상품 주문하기")
    @Test
    void order() {
        //given
        final Member member = createMember();
        final CartItem cartItem = createCartItem(member);

        RestAssured.given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(new OrderRequest(List.of(cartItem.getId())))
                .post("/orders")
                .then()
                .statusCode(CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("사용자 주문 목록 보기")
    @Test
    void getOrders() {
        //given
        final Member member = createMember();

        RestAssured.given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .get("/orders")
                .then()
                .statusCode(OK.value());
    }

    @DisplayName("특정 주문 상세 보기")
    @Test
    void getOrder() {
        //given
        final Member member = createMember();
        final CartItem cartItem = createCartItem(member);

        final String location = given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(new OrderRequest(List.of(cartItem.getId())))
                .post("/orders")
                .then()
                .extract()
                .header("Location");
        final long orderId = Long.parseLong(location.split("/")[2]);

        RestAssured.given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .get("/orders/{id}", orderId)
                .then().log().all()
                .statusCode(OK.value());
    }

    private Member createMember() {
        memberDao.addMember(new Member("test@naver.com", "1234"));
        return memberDao.getMemberByEmail("test@naver.com");
    }

    private CartItem createCartItem(final Member member) {
        Product product = insertProduct(new ProductRequest("새우깡", 50000, "http://이미지"));
        return insertCartItem(member, product);
    }

    private Product insertProduct(final ProductRequest productRequest) {
        final String locationHeader = given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(productRequest)
                .post("/products")
                .then()
                .extract()
                .header("Location");

        final long productId = Long.parseLong(locationHeader.split("/")[2]);
        return new Product(productId, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
    }

    private CartItem insertCartItem(final Member member, final Product product) {
        final String locationHeader = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(APPLICATION_JSON_VALUE)
                .body(new CartItemRequest(product.getId()))
                .post("/cart-items")
                .then().log().all()
                .extract()
                .header("Location");

        final long cartItemId = Long.parseLong(locationHeader.split("/")[2]);
        return new CartItem(cartItemId, 1, product, member);
    }

}
