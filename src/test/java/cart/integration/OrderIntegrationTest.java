package cart.integration;

import cart.dao.member.MemberDao;
import cart.dao.product.ProductDao;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.order.OrderItemResponse;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.dto.product.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    private Member 멤버_하디;
    private Member 멤버_현구막;
    private Product 상품_피자;
    private Product 상품_샐러드;
    private Product 상품_치킨;
    private Long 하디_장바구니_피자;
    private Long 하디_장바구니_치킨;
    private Long 현구막_장바구니_피자;
    private Long 현구막_장바구니_샐러드;

    @BeforeEach
    void setUp() {
        super.setUp();
        테스트_멤버_추가();
        테스트_상품_추가();
        테스트_장바구니에_아이템_추가();
    }

    @Test
    void 주문을_하면_Location_헤더가_존재하고_상태코드가_CREATED_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice());

        // when
        ExtractableResponse<Response> response = 주문(멤버_하디, orderRequest);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.header("Location")).isNotNull();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        softAssertions.assertAll();
    }

    @Test
    void 주문을_할_때_총_가격이_서버에서_계산한_상품_가격의_총합과_맞지_않으면_상태코드가_BAD_REQUEST_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice() + 400L);

        // when
        ExtractableResponse<Response> response = 주문(멤버_하디, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 다른_사람의_장바구니_아이템을_주문하려하면_조회하지못해_상태코드가_BADREQUEST_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice());

        // when
        ExtractableResponse<Response> response = 주문(멤버_현구막, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문을_한_뒤_Location_헤더_값으로_주문을_조회할_수_있다() {
        // given

        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice());
        String location = 주문(멤버_하디, orderRequest).header("Location");

        // when
        ExtractableResponse<Response> response = 주문_조회(멤버_하디, location);
        OrderResponse body = response.as(OrderResponse.class);
        List<Long> orderItemIds = body.getOrderItems()
                .stream()
                .map(OrderItemResponse::getProductId)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(orderItemIds).containsAll(List.of(상품_피자.getId(), 상품_피자.getId()));
        softAssertions.assertAll();
    }

    @Test
    void 상품_재고보다_많은_수량을_주문하면_상태코드가_BAD_REQUEST_이다() {
        // given
        장바구니_아이템_수량_변경(멤버_하디, 하디_장바구니_피자, 1000L);
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice());

        // when
        ExtractableResponse<Response> response = 주문(멤버_하디, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 멤버로_주문_목록을_조회할_수_있다() {
        // given
        OrderRequest firstOrderRequest = new OrderRequest(List.of(하디_장바구니_피자), 0L, 상품_피자.getPrice());
        OrderRequest secondOrderRequest = new OrderRequest(List.of(하디_장바구니_치킨), 0L, 상품_치킨.getPrice());

        주문(멤버_하디, firstOrderRequest);
        주문(멤버_하디, secondOrderRequest);

        // when
        ExtractableResponse<Response> response = 주문목록_조회(멤버_하디);
        List<OrderResponse> orderResponse = response.jsonPath().getList(".", OrderResponse.class);
        List<Long> orderItemIds = orderResponse.stream()
                .map(OrderResponse::getOrderItems)
                .map(orderItemResponses -> orderItemResponses.get(0))
                .map(OrderItemResponse::getProductId)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(orderResponse.size()).isEqualTo(2);
        softAssertions.assertThat(orderItemIds).containsAll(List.of(상품_피자.getId(), 상품_치킨.getId()));
        softAssertions.assertAll();
    }

    @Test
    void 주문목록이_없는_멤버의_주문목록을_조회하여도_상태코드가_OK_이다() {
        // given, when
        ExtractableResponse<Response> response = 주문목록_조회(멤버_하디);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 다른_멤버의_주문목록을_조회하면_상태코드가_FORBIDDEN_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice());
        String location = 주문(멤버_하디, orderRequest).header("Location");

        // when
        ExtractableResponse<Response> response = 주문_조회(멤버_현구막, location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void 없는_주문을_조회하면_상태코드가_BAD_REQUEST_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 0L, 상품_피자.getPrice() + 상품_치킨.getPrice());
        String location = 주문(멤버_하디, orderRequest).header("Location");

        // when
        ExtractableResponse<Response> response = 주문_조회(멤버_하디, location + 1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문시_상품들의_총_가격보다_사용_포인트가_많으면_상태코드가_BAD_REQUEST_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 1000000L, 상품_피자.getPrice() + 상품_치킨.getPrice());

        // when
        ExtractableResponse<Response> response = 주문(멤버_하디, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문시_사용_요청_포인트가_실제_보유_포인트보다_많으면_상태코드가_BAD_REQUEST_이다() {
        // given
        OrderRequest orderRequest = new OrderRequest(List.of(하디_장바구니_피자, 하디_장바구니_치킨), 20000L, 상품_피자.getPrice() + 상품_치킨.getPrice());

        // when
        ExtractableResponse<Response> response = 주문(멤버_하디, orderRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 주문(Member member, OrderRequest orderRequest) {
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

    private ExtractableResponse<Response> 주문_조회(Member member, String location) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get(location)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문목록_조회(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    private void 테스트_멤버_추가() {
        // 멤버 추가를 위한 API가 존재하지 않음
        memberDao.addMember(하디);
        멤버_하디 = memberDao.findMemberByEmail(하디.getEmail()).get();
        memberDao.addMember(현구막);
        멤버_현구막 = memberDao.findMemberByEmail(현구막.getEmail()).get();
    }

    private void 테스트_상품_추가() {
        ProductRequest 피자_생성_요청 = new ProductRequest(피자.getName(), 피자.getPrice(), 피자.getImageUrl(), 피자.getStock());
        ProductRequest 샐러드_생성_요청 = new ProductRequest(샐러드.getName(), 샐러드.getPrice(), 샐러드.getImageUrl(), 샐러드.getStock());
        ProductRequest 치킨_생성_요청 = new ProductRequest(치킨.getName(), 치킨.getPrice(), 치킨.getImageUrl(), 치킨.getStock());
        Long 피자_아이디 = Long.valueOf(상품_추가(피자_생성_요청).header("Location").split("/")[2]);
        Long 샐러드_아이디 = Long.valueOf(상품_추가(샐러드_생성_요청).header("Location").split("/")[2]);
        Long 치킨_아이디 = Long.valueOf(상품_추가(치킨_생성_요청).header("Location").split("/")[2]);

        상품_피자 = productDao.findProductById(피자_아이디).get();
        상품_샐러드 = productDao.findProductById(샐러드_아이디).get();
        상품_치킨 = productDao.findProductById(치킨_아이디).get();
    }

    private void 테스트_장바구니에_아이템_추가() {
        CartItemRequest 피자_장바구니_요청 = new CartItemRequest(상품_피자.getId());
        CartItemRequest 샐러드_장바구니_요청 = new CartItemRequest(상품_샐러드.getId());
        CartItemRequest 치킨_장바구니_요청 = new CartItemRequest(상품_치킨.getId());

        하디_장바구니_피자 = Long.valueOf(장바구니에_아이템_추가(멤버_하디, 피자_장바구니_요청).header("Location").split("/")[2]);
        하디_장바구니_치킨 = Long.valueOf(장바구니에_아이템_추가(멤버_하디, 치킨_장바구니_요청).header("Location").split("/")[2]);
        현구막_장바구니_피자 = Long.valueOf(장바구니에_아이템_추가(멤버_현구막, 피자_장바구니_요청).header("Location").split("/")[2]);
        현구막_장바구니_샐러드 = Long.valueOf(장바구니에_아이템_추가(멤버_현구막, 샐러드_장바구니_요청).header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> 상품_추가(ProductRequest product) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니에_아이템_추가(Member member, CartItemRequest cartItemRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_아이템_수량_변경(Member member, Long cartItemId, long quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }
}
