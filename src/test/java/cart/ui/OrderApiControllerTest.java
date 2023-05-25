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

    private Member member;
    private Product product;
    private CartItem cartItem;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        memberDao.addMember(new Member("test@naver.com", "1234"));
        member = memberDao.getMemberByEmail("test@naver.com");

        product = insertProduct(new ProductRequest("새우깡", 50000, "http://이미지"));
        cartItem = insertCartItem(member, product);
    }

    @DisplayName("상품 주문하기")
    @Test
    void order() {
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
        RestAssured.given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .get("/orders")
                .then()
                .statusCode(OK.value());
    }

    @DisplayName("특정 주문 상세 보기")
    @Test
    void getOrder() {
        RestAssured.given()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .get("/orders/{id}", 1)
                .then()
                .statusCode(OK.value());
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
