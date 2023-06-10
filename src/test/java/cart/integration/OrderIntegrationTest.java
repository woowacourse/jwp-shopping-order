package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderSimpleResponse;
import cart.dto.product.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;
    private Long cartItemId1;
    private Long cartItemId2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);

        Long productId1 = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        cartItemId1 = createCartItem(member, new CartItemRequest(productId1));
        cartItemId2 = createCartItem(member, new CartItemRequest(productId2));
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1), 10_000);

        ExtractableResponse<Response> response = createOrder(member, orderRequest);
        long orderId = getIdFromCreatedResponse(response);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(orderId).isNotNull();
    }


    @Test
    @DisplayName("주문 내역을 조회한다.")
    void showOrder() {
        OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1), 10_000);
        long orderId = getIdFromCreatedResponse(createOrder(member, orderRequest));

        ExtractableResponse<Response> response = showOrder(member, orderId);

        long actualId = response.jsonPath().getLong("id");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualId).isEqualTo(orderId);
    }

    @Test
    @DisplayName("전체 주문 내역을 조회한다.")
    void showOrders() {
        OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1), 10_000);
        long orderId1 = getIdFromCreatedResponse(createOrder(member, orderRequest));

        OrderRequest orderRequest2 = new OrderRequest(List.of(cartItemId2), 15_000);
        long orderId2 = getIdFromCreatedResponse(createOrder(member, orderRequest2));

        ExtractableResponse<Response> response = showOrders(member, 2, 1);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> resultCartItemIds = response.jsonPath()
                .getList(".", OrderSimpleResponse.class).stream()
                .map(OrderSimpleResponse::getId)
                .collect(Collectors.toList());
        Assertions.assertThat(resultCartItemIds).containsAll(Arrays.asList(orderId1, orderId2));
    }

    @Test
    @DisplayName("주문 시 할인 정책을 조회한다.")
    void showDiscountPolicies() {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("orders/discount-policies")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> showOrders(final Member member, int size, int page) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders?unit-size={size}&page={page}", size, page)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> showOrder(final Member member, final Long orderId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", orderId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> createOrder(final Member member, final OrderRequest orderRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }


    private Long createCartItem(final Member member, final CartItemRequest cartItemRequest) {
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

    private Long createProduct(final ProductRequest productRequest) {
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

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
