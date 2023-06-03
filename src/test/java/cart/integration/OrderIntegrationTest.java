package cart.integration;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.dto.CartItemRequest;
import cart.dto.OrderItemDto;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderedProduct;
import cart.dto.PageInfo;
import cart.dto.PageRequest;
import cart.dto.PagingOrderResponse;
import cart.dto.PaymentDto;
import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cart.common.fixture.DomainFixture.EMAIL;
import static cart.common.fixture.DomainFixture.PASSWORD;
import static cart.common.fixture.DomainFixture.PRODUCT_IMAGE;
import static cart.common.fixture.DomainFixture.PRODUCT_NAME;
import static cart.common.step.CartItemStep.addCartItemAndGetId;
import static cart.common.step.CartItemStep.showCartItemByProductId;
import static cart.common.step.OrderStep.addOrder;
import static cart.common.step.OrderStep.addOrderAndGetId;
import static cart.common.step.ProductStep.addProductAndGetId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long cartItemId;
    private Long productId;


    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        new MemberDao(jdbcTemplate).addMember(new MemberEntity(EMAIL, PASSWORD, 1000));
        productId = addProductAndGetId(new ProductRequest("chicken", 20000, "chicken.jpeg"));
        cartItemId = addCartItemAndGetId(EMAIL, PASSWORD, new CartItemRequest(productId));
    }

    @Test
    void 전체_주문을_조회한다() {
        //given
        final PageRequest pageRequest = new PageRequest(1, 10);

        addOrder(EMAIL, PASSWORD, new OrderRequest(List.of(new OrderItemDto(cartItemId)), new PaymentDto(20000, 19000, 1000)));

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .param("size", pageRequest.getSize())
                .param("page", pageRequest.getPage())
                .when()
                .get("/orders")
                .then()
                .log().all().extract();

        //then
        final PagingOrderResponse orderResponse = response.as(PagingOrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(orderResponse.getOrderResponses()).usingRecursiveComparison()
                    .isEqualTo(
                            List.of(new OrderResponse(
                                    1L,
                                    List.of(new OrderedProduct(PRODUCT_NAME, 20000, 1, PRODUCT_IMAGE)),
                                    new PaymentDto(20000, 19000, 1000))
                            ));
            softly.assertThat(orderResponse.getPageInfo()).usingRecursiveComparison()
                    .isEqualTo(new PageInfo(1, 10, 1, 1));
        });
    }

    @Test
    void 특정_주문을_조회한다() {
        //given
        final Long orderId = addOrderAndGetId(EMAIL, PASSWORD, new OrderRequest(List.of(new OrderItemDto(cartItemId)), new PaymentDto(20000, 19000, 1000)));

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .pathParam("orderId", orderId)
                .when()
                .get("/orders/{orderId}")
                .then()
                .log().all().extract();

        //then
        final OrderResponse orderResponse = response.as(OrderResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(orderResponse).usingRecursiveComparison()
                    .isEqualTo(new OrderResponse(
                                    1L,
                                    List.of(new OrderedProduct(PRODUCT_NAME, 20000, 1, PRODUCT_IMAGE)),
                                    new PaymentDto(20000, 19000, 1000)
                            )
                    );
        });
    }

    @Nested
    class 주문_테스트 {
        @Test
        void 주문한다() {
            //given
            final OrderRequest orderRequest = new OrderRequest(List.of(new OrderItemDto(cartItemId)), new PaymentDto(20000, 19000, 1000));

            //when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().basic(EMAIL, PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(orderRequest)
                    .when()
                    .post("/orders")
                    .then()
                    .log().all().extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        void 주문한_상품은_장바구니에서_삭제된다() {
            //given
            addOrder(EMAIL, PASSWORD, new OrderRequest(List.of(new OrderItemDto(cartItemId)), new PaymentDto(20000, 19000, 1000)));

            //when
            final ExtractableResponse<Response> response = showCartItemByProductId(EMAIL, PASSWORD, productId);

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                softly.assertThat(response.body().asString()).isEqualTo("회원의 장바구니에 해당 상품이 존재하지 않습니다; productId=1");
            });
        }

        @Test
        void 주문할_때_결제_정보를_입력하지_않으면_예외를_던진다() {
            //given
            final PaymentDto paymentWithNull = new PaymentDto(null, 19000, 1000);

            //when
            final ExtractableResponse<Response> response = addOrder(EMAIL, PASSWORD, new OrderRequest(List.of(new OrderItemDto(cartItemId)), paymentWithNull));

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().asString()).contains("초기 결제금액이 입력되지 않았습니다.");
            });
        }

        @Test
        void 주문할_때_주문_상품_정보를_입력하지_않으면_예외를_던진다() {
            //given
            final OrderItemDto nullOrderItem = new OrderItemDto(null);

            //when
            final ExtractableResponse<Response> response = addOrder(EMAIL, PASSWORD, new OrderRequest(List.of(nullOrderItem), new PaymentDto(20000, 19000, 1000)));

            //then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().asString()).contains("장바구니 상품 id가 입력되지 않았습니다.");
            });
        }
    }
}
