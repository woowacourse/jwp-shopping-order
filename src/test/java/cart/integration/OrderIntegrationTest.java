
package cart.integration;

import cart.application.CartItemService;
import cart.application.OrderService;
import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.domain.Member;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.request.ProductRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Long productId3;
    private Long productId4;
    private Member member;
    private Member member2;
    private int totalPrice;
    private int discountPrice;

    @BeforeEach
    void setUp() {
        super.setUp();
        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        productId3 = createProduct(new ProductRequest("냉면", 5_000, "abc"));
        productId4 = createProduct(new ProductRequest("족발", 30_000, "def"));

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);

        totalPrice = 50_000;
        discountPrice = 45_000;
    }

    @DisplayName("주문을 추가한다.")
    @Test
    void addOrder() {
        OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(productId, 2), new OrderItemRequest(productId2, 3)), "2023-06-03T06:40:51.437Z");

        ExtractableResponse<Response> response = requestAddOrder(member, orderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("단일 주문을 조회한다")
    @Test
    void findOrderById() {
        OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(productId, 2), new OrderItemRequest(productId2, 3)), "2023-06-03T06:40:51.437Z");

        ExtractableResponse<Response> addResponse = requestAddOrder(member, orderRequest);
        long orderId = getIdFromCreatedResponse(addResponse);

        OrderResponse orderResponse = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders/" + orderId)
                .then().log().all()
                .extract().jsonPath().getObject(".", OrderResponse.class);

        assertThat(orderResponse.getOrderId()).isEqualTo(orderId);
    }

    @DisplayName("사용자의 모든 주문을 조회한다")
    @Test
    void findAllOrdersByMember() {
        OrderRequest orderRequest1 = new OrderRequest(List.of(new OrderItemRequest(productId, 2), new OrderItemRequest(productId2, 3)), "2023-06-03T06:40:51.437Z");
        OrderRequest orderRequest2 = new OrderRequest(List.of(new OrderItemRequest(productId3, 4), new OrderItemRequest(productId4, 5)), "2023-06-03T06:40:51.437Z");

        ExtractableResponse<Response> addResponse1 = requestAddOrder(member, orderRequest1);
        ExtractableResponse<Response> addResponse2 = requestAddOrder(member, orderRequest2);

        long orderId1 = getIdFromCreatedResponse(addResponse1);
        long orderId2 = getIdFromCreatedResponse(addResponse2);


        ExtractableResponse<Response> response = requestGetAllOrders();

        OrdersResponse ordersResponse = response.jsonPath().getObject(".", OrdersResponse.class);
    }

    @DisplayName("주문 Id에 해당하는 주문 내역을 삭제한다.")
    @Test
    void removeOrder() {

        OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemRequest(productId, 2), new OrderItemRequest(productId2, 3)), "2023-06-03T06:40:51.437Z");
        ExtractableResponse<Response> addOrderResponse = requestAddOrder(member, orderRequest);
        long orderId = getIdFromCreatedResponse(addOrderResponse);

        ExtractableResponse<Response> response = requestDeleteOrder(orderId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> getAllOrdersResponse = requestGetAllOrders();

        OrdersResponse ordersResponse = getAllOrdersResponse.jsonPath().getObject(".", OrdersResponse.class);

        Optional<OrderResponse> selectedOrderItemResponse = ordersResponse.getOrders().stream()
                .filter(orderResponse -> orderResponse.getOrderId().equals(orderId))
                .findFirst();

        assertThat(selectedOrderItemResponse.isPresent()).isFalse();
    }

    private Long createProduct(ProductRequest productRequest) {

        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/products")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddOrder(Member member, OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when().post("/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestGetAllOrders() {
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestDeleteOrder(Long id) {
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().delete("/orders/" + id)
                .then().log().all()
                .extract();
    }
}
