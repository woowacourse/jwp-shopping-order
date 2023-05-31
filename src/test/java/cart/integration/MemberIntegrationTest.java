package cart.integration;

import cart.application.OrderedItemDao;
import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderedItemDao orderedItemDao;
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

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    @Test
    @DisplayName("특정 멤버의 특정 주문을 조회한다")
    void get_order() {
        Long cartItemId = createCartItem(member2, new CartItemRequest(productId2));
        Long cartItemId2 = createCartItem(member2, new CartItemRequest(productId));
        Long orderId = requestOrder(member2, cartItemId, cartItemId2);
        ExtractableResponse<Response> response = requestGetOrder(member2, orderId);
        OrderResponse orderResponse = getOrderResponse(member2, orderId);


        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getDiscountedTotalItemPrice()).isEqualTo(14_000);
        assertThat(orderResponse.getTotalItemPrice()).isEqualTo(20_000);
        assertThat(orderResponse.getShippingFee()).isEqualTo(3_000);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(17_000);
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

    private Long requestOrder(Member member, Long cartItemId, Long cartItemId2) {
        List<Long> cartItemIds = List.of(cartItemId, cartItemId2);
        int totalItemDiscountAmount = 6_000;
        int totalMemberDiscountAmount = 1_000;

        int totalItemPrice = 20_000;
        int discountedTotalItemPrice = 14_000;
        int shippingFee = 3_000;
        int totalPrice = 17_000;

        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(new OrderCreateRequest(
                        cartItemIds, totalItemDiscountAmount, totalMemberDiscountAmount,
                        totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice
                ))
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private ExtractableResponse<Response> requestGetOrder(Member member, Long orderId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/members/" + member.getId() + "/orders/" + orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private OrderResponse getOrderResponse(Member member, Long orderId) {

        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/members/" + member.getId() + "/orders/" + orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", OrderResponse.class);
    }
}
