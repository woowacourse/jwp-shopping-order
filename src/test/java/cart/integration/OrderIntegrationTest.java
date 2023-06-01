package cart.integration;

import cart.domain.Member;
import cart.domain.repository.MemberRepository;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.ProductFixture.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private Long productId;
    private Long productId2;
    private Long productId3;

    private Long cartId;
    private Long cartId2;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(치킨.REQUEST);
        productId2 = createProduct(피자.REQUEST);
        productId3 = createProduct(핫도그.REQUEST);

        member = memberRepository.findById(1L).orElseGet(null);

        cartId = requestAddCartItem(member, new CartItemRequest(productId));
        cartId2 = requestAddCartItem(member, new CartItemRequest(productId2));
    }

    @DisplayName("주문을 한 뒤 장바구니가 삭제된다")
    @Test
    void removeCartItemAfterOrderItem() {
        orderCartItems(member, cartId, cartId2);
        ExtractableResponse<Response> cartItemSelectResponse = requestGetCartItems(member);

        CartItemResponse[] cartItemResponses = cartItemSelectResponse.as(CartItemResponse[].class);
        List<Long> currentCartIds = Arrays.stream(cartItemResponses).map(CartItemResponse::getId).collect(Collectors.toList());

        assertThat(currentCartIds).doesNotContain(cartId, cartId2);
    }

    @DisplayName("주문을 한 뒤 사용자의 포인트가 10% 적립된다")
    @Test
    void addPointAfterOrderItem() {
        Integer prevPoint = member.getPoint();
        int price = 치킨.PRODUCT.getPrice() + 피자.PRODUCT.getPrice();
        int point = (price / 100) * 10;

        orderCartItems(member, 0, cartId, cartId2);
        requestGetCartItems(member);

        Member afterOrder = memberRepository.findById(member.getId()).orElseGet(null);
        assertThat(afterOrder.getPoint()).isEqualTo(prevPoint + point);
    }

    @DisplayName("주문을 한 뒤 사용자의 사용 포인트를 제외한 결제금액의 포인트가 10% 적립된다")
    @Test
    void addPointAfterOrderItem2() {
        int prevPoint = member.getPoint();
        int usePoint = 100;
        int price = 치킨.PRODUCT.getPrice() + 피자.PRODUCT.getPrice() - usePoint;
        int point = (price / 100) * 10;

        orderCartItems(member, usePoint, cartId, cartId2);

        Member afterOrder = memberRepository.findById(member.getId()).orElseGet(null);
        assertThat(afterOrder.getPoint()).isEqualTo(prevPoint + point - usePoint);
    }

    private ExtractableResponse<Response> orderCartItems(Member member, Long... id) {
        List<Long> cartIds = Arrays.stream(id).collect(Collectors.toList());

        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(cartIds, 0))
                .when()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .post("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> orderCartItems(Member member, Integer point, Long... id) {
        List<Long> cartIds = Arrays.stream(id).collect(Collectors.toList());

        return given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(cartIds, point))
                .when()
                .log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .post("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    @DisplayName("장바구니에 아이템을 추가한뒤 주문을 할 수 있다")
    @Test
    void orderItem() {
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(List.of(cartId, cartId2), 0))
                .when()
                .log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .post("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }


    private Long requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
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
}
