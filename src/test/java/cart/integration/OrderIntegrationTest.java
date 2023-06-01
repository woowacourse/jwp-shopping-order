package cart.integration;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.dto.CartItemRequest;
import cart.dto.OrderInfo;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.OrderedProduct;
import cart.dto.PaymentDto;
import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
import static cart.common.step.CartItemStep.addCartItem;
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

    private Long productId;


    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        new MemberDao(jdbcTemplate).addMember(new MemberEntity(EMAIL, PASSWORD, 1000));
        productId = addProductAndGetId(new ProductRequest("chicken", 20000, "chicken.jpeg"));
        addCartItem(EMAIL, PASSWORD, new CartItemRequest(productId));
    }

    @Test
    void 주문한다() {
        //given
        final OrderRequest orderRequest = new OrderRequest(List.of(new OrderInfo(productId, 1)), 19000, 1000);

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
    void 전체_주문을_조회한다() {
        //given
        addOrder(EMAIL, PASSWORD, new OrderRequest(List.of(new OrderInfo(productId, 1)), 19000, 1000));

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when()
                .get("/orders")
                .then()
                .log().all().extract();

        //then
        final List<OrderResponse> orderResponses = response.as(new TypeRef<>() {
        });

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(orderResponses).usingRecursiveComparison()
                    .isEqualTo(
                            List.of(new OrderResponse(
                                    1L,
                                    List.of(new OrderedProduct(PRODUCT_NAME, 20000, 1, PRODUCT_IMAGE)),
                                    new PaymentDto(20000, 19000, 1000))
                            ));
        });
    }

    @Test
    void 특정_주문을_조회한다() {
        //given
        final Long orderId = addOrderAndGetId(EMAIL, PASSWORD, new OrderRequest(List.of(new OrderInfo(productId, 1)), 19000, 1000));

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
}
