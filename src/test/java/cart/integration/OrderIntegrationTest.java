package cart.integration;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.MEMBER_B;
import static cart.fixture.DomainFixture.PIZZA;
import static cart.fixture.DomainFixture.SALAD;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemRequest;
import cart.dto.request.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    Member memberA;
    Member memberB;

    Product chicken;
    Product salad;
    Product pizza;

    @BeforeEach
    void setUp() {
        super.setUp();

        memberA = memberRepository.findByEmail(MEMBER_A.getEmail()).get();
        memberB = memberRepository.findByEmail(MEMBER_B.getEmail()).get();

        chicken = productRepository.findById(CHICKEN.getId()).get();
        salad = productRepository.findById(SALAD.getId()).get();
        pizza = productRepository.findById(PIZZA.getId()).get();
    }

    @Test
    @DisplayName("장바구니에 치킨과 샐러드를 추가한 뒤 장바구니 상품 전체를 주문한다.")
    void order() {
        Long chickenCartItemId = requestAddCartItem(memberB, new CartItemRequest(chicken.getId()));
        Long saladCartItemId = requestAddCartItem(memberB, new CartItemRequest(salad.getId()));

        OrderRequest orderRequest = new OrderRequest(0, List.of(chickenCartItemId, saladCartItemId));

        ExtractableResponse<Response> response = requestAddOrder(memberB, orderRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).contains("/orders/")
        );
    }

    @Test
    @DisplayName("주문할 때 소지 포인트 이상으로 사용 포인트를 전달하면 실패한다.")
    void orderWithGreaterThanCurrentPoints() {
        Long chickenCartItemId = requestAddCartItem(memberB, new CartItemRequest(chicken.getId()));
        Long saladCartItemId = requestAddCartItem(memberB, new CartItemRequest(salad.getId()));

        OrderRequest orderRequest = new OrderRequest(5_000, List.of(chickenCartItemId, saladCartItemId));

        ExtractableResponse<Response> response = requestAddOrder(memberB, orderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("주문할 때 최대 사용 가능 포인트 이상으로 사용 포인트를 전달하면 실패한다.")
    void orderWithGreaterThanConditionPoints() {
        Long pizzaCartItemId = requestAddCartItem(memberA, new CartItemRequest(pizza.getId()));

        OrderRequest orderRequest = new OrderRequest(1_301, List.of(pizzaCartItemId));

        ExtractableResponse<Response> response = requestAddOrder(memberA, orderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> requestAddOrder(Member member, OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    private Long requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        return Long.parseLong(given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract()
                .header("Location")
                .split("/")[2]);
    }

    @Test
    @DisplayName("주문을 세 번 한 뒤, 주문 목록 조회를 요청한다.")
    void orders() {
        Long chickenCartItemId;
        Long saladCartItemId;
        OrderRequest orderRequest;

        chickenCartItemId = requestAddCartItem(memberB, new CartItemRequest(chicken.getId()));
        saladCartItemId = requestAddCartItem(memberB, new CartItemRequest(salad.getId()));
        orderRequest = new OrderRequest(0, List.of(chickenCartItemId, saladCartItemId));
        Long firstOrderId = requestAddOrderAndGetOrderId(memberB, orderRequest);

        chickenCartItemId = requestAddCartItem(memberB, new CartItemRequest(chicken.getId()));
        saladCartItemId = requestAddCartItem(memberB, new CartItemRequest(salad.getId()));
        orderRequest = new OrderRequest(0, List.of(chickenCartItemId, saladCartItemId));
        Long secondOrderId = requestAddOrderAndGetOrderId(memberB, orderRequest);

        chickenCartItemId = requestAddCartItem(memberB, new CartItemRequest(chicken.getId()));
        saladCartItemId = requestAddCartItem(memberB, new CartItemRequest(salad.getId()));
        orderRequest = new OrderRequest(0, List.of(chickenCartItemId, saladCartItemId));
        Long thirdOrderId = requestAddOrderAndGetOrderId(memberB, orderRequest);

        ExtractableResponse<Response> response = requestFirstOrdersPage(memberB);

        assertAll(
                () -> assertThat(response.jsonPath().getLong("orders[0].id")).isEqualTo(thirdOrderId),
                () -> assertThat(response.jsonPath().getLong("orders[1].id")).isEqualTo(secondOrderId),
                () -> assertThat(response.jsonPath().getLong("orders[2].id")).isEqualTo(firstOrderId)
        );
    }

    private ExtractableResponse<Response> requestFirstOrdersPage(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    @Test
    @DisplayName("주문을 한 번 한 뒤, 해당 주문 상세 보기를 요청한다.")
    void orderById() {
        Long chickenCartItemId = requestAddCartItem(memberB, new CartItemRequest(chicken.getId()));
        Long saladCartItemId = requestAddCartItem(memberB, new CartItemRequest(salad.getId()));
        OrderRequest orderRequest = new OrderRequest(0, List.of(chickenCartItemId, saladCartItemId));
        Long orderId = requestAddOrderAndGetOrderId(memberB, orderRequest);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(memberB.getEmail(), memberB.getPassword())
                .pathParam("id", orderId)
                .when()
                .get("/orders/{id}")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(orderId),
                () -> assertThat(response.jsonPath().getLong("orderItems[0].productId")).isEqualTo(chicken.getId()),
                () -> assertThat(response.jsonPath().getLong("orderItems[1].productId")).isEqualTo(salad.getId())
        );
    }

    private Long requestAddOrderAndGetOrderId(Member member, OrderRequest orderRequest) {
        return Long.parseLong(requestAddOrder(member, orderRequest)
                .header("Location")
                .split("/")[2]);
    }
}
