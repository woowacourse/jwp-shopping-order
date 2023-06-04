package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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
        member = memberDao.getMemberById(1L); //일반 등급
        member2 = memberDao.getMemberById(2L); //실버 등급
    }

    @AfterEach
    void setAfter(){

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
    @Transactional
    public void createOrder() {
        //given
        productId = createProduct(new ProductRequest("연어덮밥", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("카레", 10_000, "http://example.com/pizza.jpg", 50));

        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId2, 1));

        //when
        var response = requestOrder(member2, cartItemId4);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> requestOrder(Member member, Long cartItemId) {
        List<Long> cartItemIds = List.of(cartItemId);
        int totalItemDiscountAmount = 5_000;
        int totalMemberDiscountAmount = 0;

        int totalItemPrice = 10_000;
        int discountedTotalItemPrice = 5_000;
        int shippingFee = 3_000;
        int totalPrice = 8_000;

        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member2.getPassword())
                .body(new OrderCreateRequest(
                        cartItemIds, totalItemDiscountAmount, totalMemberDiscountAmount,
                        totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice
                ))
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
    @DisplayName("상품 할인 적용")
    @Transactional
    public void product_discount() {
        //given
        productId = createProduct(new ProductRequest("족발", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("마라탕", 10_000, "http://example.com/pizza.jpg", 50));

        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId2, 1));
        List<Long> cartItemIds = List.of(cartItemId4);
        int totalItemDiscountAmount = 5_000;
        int totalMemberDiscountAmount = 0;

        int totalItemPrice = 10_000;
        int discountedTotalItemPrice = 5_000;
        int shippingFee = 3_000;
        int totalPrice = 8_000;

        //when
        var location = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .body(new OrderCreateRequest(
                        cartItemIds, totalItemDiscountAmount, totalMemberDiscountAmount,
                        totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice
                ))
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        OrderResponse orderResponse = given().log().all()
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", OrderResponse.class);

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getDiscountedTotalItemPrice()).isEqualTo(5_000);
    }

    @Test
    @DisplayName("멤버 할인 적용")
    public void member_discount() {
        //given
        productId = createProduct(new ProductRequest("덮밥", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("규동", 10_000, "http://example.com/pizza.jpg", 50));

        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId, 1));
        List<Long> cartItemIds = List.of(cartItemId4);
        int totalItemDiscountAmount = 0;
        int totalMemberDiscountAmount = 1_000;

        int totalItemPrice = 10_000;
        int discountedTotalItemPrice = 9_000;
        int shippingFee = 3_000;
        int totalPrice = 12_000;

        //when
        var location = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .body(new OrderCreateRequest(
                        cartItemIds, totalItemDiscountAmount, totalMemberDiscountAmount,
                        totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice
                ))
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        OrderResponse orderResponse = given().log().all()
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", OrderResponse.class);

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getDiscountedTotalItemPrice()).isEqualTo(9_000);
    }

    @Test
    @DisplayName("상품의 원가격, 할인된 가격, 배송비, 총구매금액을 구한다")
    @Transactional
    public void testAllPrices() {
        //given
        productId = createProduct(new ProductRequest("짬뽕", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("탕수육", 10_000, "http://example.com/pizza.jpg", 50));

        Long cartItemId4 = createCartItem(member2, new CartItemRequest(productId2, 1));
        List<Long> cartItemIds = List.of(cartItemId4);
        int totalItemDiscountAmount = 5_000;
        int totalMemberDiscountAmount = 0;

        int totalItemPrice = 10_000;
        int discountedTotalItemPrice = 5_000;
        int shippingFee = 3_000;
        int totalPrice = 8_000;

        //when
        var location = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .body(new OrderCreateRequest(
                        cartItemIds, totalItemDiscountAmount, totalMemberDiscountAmount,
                        totalItemPrice, discountedTotalItemPrice, shippingFee, totalPrice))
                .when()
                .post("/orders")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        OrderResponse orderResponse = given().log().all()
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", OrderResponse.class);

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getDiscountedTotalItemPrice()).isEqualTo(5_000);
        assertThat(orderResponse.getTotalItemPrice()).isEqualTo(10_000);
        assertThat(orderResponse.getShippingFee()).isEqualTo(3_000);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(8_000);
    }

    @Test
    @DisplayName("특정 멤버의 특정 주문을 조회한다")
    @Transactional
    void get_order() {
        productId = createProduct(new ProductRequest("초밥", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("김치찜", 10_000, "http://example.com/pizza.jpg", 50));

        Long cartItemId = createCartItem(member2, new CartItemRequest(productId2, 1));
        Long cartItemId2 = createCartItem(member2, new CartItemRequest(productId, 1));
        Long orderId = requestCreateOrder(member2, List.of(cartItemId, cartItemId2));
        ExtractableResponse<Response> response = requestGetOrderById(member2, orderId);
        OrderResponse orderResponse = getOrderResponse(member2, orderId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getDiscountedTotalItemPrice()).isEqualTo(14_000);
        assertThat(orderResponse.getTotalItemPrice()).isEqualTo(20_000);
        assertThat(orderResponse.getShippingFee()).isEqualTo(3_000);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(17_000);
    }

    private Long requestCreateOrder(Member member, List<Long> cartItemIds) {
        int totalItemDiscountAmount = 5_000;
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

    private ExtractableResponse<Response> requestGetOrderById(Member member, Long orderId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private OrderResponse getOrderResponse(Member member, Long orderId) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", OrderResponse.class);
    }

    @Test
    @DisplayName("멤버의 모든 주문들을 조회한다")
    @Transactional
    public void getOrders() {
        var result = requestGetAllOrders(member2);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("멤버의 모든 주문을 조회했을 때 실제 주문이 존재하는지 확인한다")
    @Transactional
    public void getOrders_order_exist() {
        productId = createProduct(new ProductRequest("감자탕", 10_000, "http://example.com/chicken.jpg", 0));
        productId2 = createProduct(new ProductRequest("칼국수", 10_000, "http://example.com/pizza.jpg", 50));

        Long cartItemId = createCartItem(member2, new CartItemRequest(productId2, 1));
        Long cartItemId2 = createCartItem(member2, new CartItemRequest(productId, 1));

        Long orderId = requestCreateOrder(member2, List.of(cartItemId, cartItemId2));

        ExtractableResponse<Response> allOrdersResponse = requestGetAllOrders(member2);

        Optional<OrderResponse> selectedCartItemResponse = allOrdersResponse.jsonPath()
                .getList(".", OrderResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(orderId))
                .findFirst();

        assertThat(selectedCartItemResponse.isPresent()).isNotNull();
    }

    private ExtractableResponse<Response> requestGetAllOrders(Member member) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .extract();
    }
}
