package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("피자", 10_000, "http://example.com/pizza.jpg", 50));

        member = memberDao.getMemberById(1L); //일반 등급
        member2 = memberDao.getMemberById(2L); //실버 등급
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

    private Long createCartItem(Member member, CartItemRequest cartItemRequest) {

        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    @Test
    public void createOrder() {
        //given
        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId2));

        //when
        var response = requestOrder(member2, cartItemId4);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

//    @DisplayName("장바구니에 아이템이 멤버의 등급에 따라 가격이 할인된다")
//    @Test
//    void addCartItem() {
//        //given
//        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId2));
//
//        //when
//        requestOrder(member2, cartItemId4);
//        ExtractableResponse<Response> response = requestGetCartItems(member2);
//
//        Optional<CartItemResponse> selectedCartItemResponse = response.jsonPath()
//                .getList(".", CartItemResponse.class)
//                .stream()
//                .filter(cartItemResponse -> cartItemResponse.getId().equals(productId2))
//                .findFirst();
//
//
//        assertThat(selectedCartItemResponse.get().get).isEqualTo(5_000);
//    }

    private ExtractableResponse<Response> requestOrder(Member member, Long cartItemId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member2.getPassword())
                .body(new OrderCreateRequest(List.of(
                        new OrderItemRequest(cartItemId, 1)
                )))
                .when()
                .post("/orders")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> requestGetCartItems(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    @Test
    public void getCreatedOrder() {
        //given
        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId2));

        //when
        var location = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .body(new OrderCreateRequest(List.of(
                        new OrderItemRequest(cartItemId4, 1)
                )))
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        System.out.println(location);

        // create product
//        var location1 =
//                given()
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .body(product)
//                        .when()
//                        .post("/products")
//                        .then()
//                        .statusCode(HttpStatus.CREATED.value())
//                        .extract().header("Location");


        OrderResponse orderResponse = given().log().all()
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", OrderResponse.class);


        // get product
//        var responseProduct = given().log().all()
//                .when()
//                .get(location)
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .extract()
//                .jsonPath()
//                .getObject(".", ProductResponse.class);

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getDiscountedTotalItemPrice()).isEqualTo(5_000);
    }
}
