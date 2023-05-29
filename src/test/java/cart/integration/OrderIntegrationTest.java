package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
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
    private Long productId;
    private Long productId2;
    private Long productId3;
    private Member member1;
    private Member member2;
    private OrderRequest orderRequest1;
    private OrderRequest orderRequest2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        productId3 = createProduct(new ProductRequest("셀러드", 20_000, "http://example.com/salad.jpg"));

        member1 = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);

        orderRequest1 = new OrderRequest(List.of(new OrderItemRequest(productId, 3), new OrderItemRequest(productId2,
            3)));
        orderRequest2 = new OrderRequest(List.of(new OrderItemRequest(productId2, 1), new OrderItemRequest(productId3,
            5)));
    }

    @DisplayName("장바구니에 담긴 상품을 주문하고 주문내역을 저장한다.")
    @Test
    public void saveOrder() {
        //given
        //when
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
            .body(orderRequest1)
            .when()
            .post("/cart-items/order")
            .then().log().all()
            .extract();

        final OrderResponse orderResponse = response.body().as(OrderResponse.class);

        //then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isNotNull(),
            () -> assertThat(orderResponse.getItems()).hasSize(2),
            () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(75_000),
            () -> assertThat(orderResponse.getDiscountPrice()).isEqualTo(5000),
            () -> assertThat(orderResponse.getDeliveryFee()).isEqualTo(3000),
            () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(73000)
        );
    }

    @DisplayName("주문내역의 ID를 통해 단일 주문내역을 조회한다.")
    @Test
    public void findOrderById() {
        //given
        final Long orderId = createOrder(orderRequest1);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
            .when()
            .get("/orders/{orderId}", orderId)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        final OrderResponse orderResponse = response.body().as(OrderResponse.class);

        //then
        assertAll(
            () -> assertThat(orderResponse.getOrderId()).isEqualTo(orderId),
            () -> assertThat(orderResponse.getItems()).hasSize(2),
            () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(75_000),
            () -> assertThat(orderResponse.getDiscountPrice()).isEqualTo(5000),
            () -> assertThat(orderResponse.getDeliveryFee()).isEqualTo(3000),
            () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(73000)
        );
    }

    @DisplayName("멤버의 전체 주문내역을 조회한다.")
    @Test
    public void findOrders() {
        //given
        createOrder(orderRequest1);
        createOrder(orderRequest2);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
            .when()
            .get("/orders")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        final OrdersResponse ordersResponse = response.body().as(OrdersResponse.class);

        //then
        assertAll(
            () -> assertThat(ordersResponse.getOrders()).hasSize(2),
            
            () -> assertThat(ordersResponse.getOrders().get(0).getItems()).hasSize(2),
            () -> assertThat(ordersResponse.getOrders().get(0).getTotalPrice()).isEqualTo(75_000),
            () -> assertThat(ordersResponse.getOrders().get(0).getDiscountPrice()).isEqualTo(5000),
            () -> assertThat(ordersResponse.getOrders().get(0).getDeliveryFee()).isEqualTo(3000),
            () -> assertThat(ordersResponse.getOrders().get(0).getTotalPrice()).isEqualTo(73000),

            () -> assertThat(ordersResponse.getOrders().get(1).getItems()).hasSize(2),
            () -> assertThat(ordersResponse.getOrders().get(1).getTotalPrice()).isEqualTo(115_000),
            () -> assertThat(ordersResponse.getOrders().get(1).getDiscountPrice()).isEqualTo(5000),
            () -> assertThat(ordersResponse.getOrders().get(1).getDeliveryFee()).isEqualTo(3000),
            () -> assertThat(ordersResponse.getOrders().get(1).getTotalPrice()).isEqualTo(113_000)
        );
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

    public Long createOrder(OrderRequest orderRequest) {
        final ExtractableResponse<Response> response = given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
            .body(orderRequest)
            .when()
            .post("/cart-items/order")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
