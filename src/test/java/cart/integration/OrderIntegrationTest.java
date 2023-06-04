package cart.integration;

import cart.domain.carts.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.order.OrderProductResponse;
import cart.dto.order.OrderProductsRequest;
import cart.dto.order.OrderResponse;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
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

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderIntegrationTest extends IntegrationTest {

    private static final int DELIVERY_FEE = 3_000;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CartItemDao cartItemDao;

    private static final int USED_POINT = 100;

    private Member member;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private List<Long> orderItems;

    @BeforeEach
    void setUp() {
        super.setUp();

        memberDao = new MemberDao(jdbcTemplate);

        member = memberDao.getMemberById(1L);

        cartItem1 = cartItemDao.findById(1L);
        cartItem2 = cartItemDao.findById(2L);

        orderItems = List.of(1L, 2L);
    }

    @Test
    void 사용자는_장바구니에_담긴_상품을_주문할_수_있다() {
        ExtractableResponse<Response> response = requestPostOrderProducts(member, orderItems, USED_POINT);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getIdFromCreatedResponse(response)).isGreaterThan(0);
    }

    @Test
    void 잘못된_사용자_정보로_주문_요청시_실패한다() {
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");

        ExtractableResponse<Response> response = requestPostOrderProducts(illegalMember, orderItems, USED_POINT);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 장바구니에_없는_상품에_대한_주문_요청시_실패한다() {
        List<Long> illegalOrderItems = List.of(0L);

        ExtractableResponse<Response> response = requestPostOrderProducts(member, illegalOrderItems, USED_POINT);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문_요청시_포인트가_음수인_경우_실패한다() {
        int illegalUsedPoint = -1;

        ExtractableResponse<Response> response = requestPostOrderProducts(member, orderItems, illegalUsedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문_요청시_포인트가_사용자의_소유_포인트보다_많은_경우_실패한다() {
        int illegalUsedPoint = member.getPoint() + 100;

        ExtractableResponse<Response> response = requestPostOrderProducts(member, orderItems, illegalUsedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 사용자의_주문_상품을_조회한다() {
        ExtractableResponse<Response> orderProductsResponse = requestPostOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItems(member);
        List<OrderResponse> orderResponses = response.body().jsonPath().getList(".", OrderResponse.class);
        OrderResponse orderResponse = orderResponses.get(0);

        Product product1 = cartItem1.getProduct();
        OrderProductResponse orderProductResponse1 = new OrderProductResponse(product1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl(), cartItem1.getQuantity(), cartItem1.calculateTotalProductsPrice());
        Product product2 = cartItem2.getProduct();
        OrderProductResponse orderProductResponse2 = new OrderProductResponse(product2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl(), cartItem2.getQuantity(), cartItem2.calculateTotalProductsPrice());

        assertSoftly(softly -> {
            softly.assertThat(orderResponse.getOrderId()).isEqualTo(orderId);
            softly.assertThat(orderResponse.getOrderProducts().toString()).contains(orderProductResponse1.toString(), orderProductResponse2.toString());
            softly.assertThat(orderResponse.getOrderTotalPrice()).isEqualTo(102900);
            softly.assertThat(orderResponse.getUsedPoint()).isEqualTo(100);
        });
    }

    private OrderProductResponse toOrderProductResponse(CartItem cartItem) {
        return new OrderProductResponse(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getProduct().getImageUrl(),
                cartItem.getQuantity(),
                cartItem.calculateTotalProductsPrice()
        );
    }

    @Test
    void 사용자의_주문_상품_상세정보를_조회한다() {
        ExtractableResponse<Response> orderProductsResponse = requestPostOrderProducts(member, orderItems, USED_POINT);
        Long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItemsDetail(member, orderId);
        OrderResponse orderResponse = response.body().jsonPath().getObject(".", OrderResponse.class);

        Product product1 = cartItem1.getProduct();
        OrderProductResponse orderProductResponse1 = new OrderProductResponse(product1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl(), cartItem1.getQuantity(), cartItem1.calculateTotalProductsPrice());
        Product product2 = cartItem2.getProduct();
        OrderProductResponse orderProductResponse2 = new OrderProductResponse(product2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl(), cartItem2.getQuantity(), cartItem2.calculateTotalProductsPrice());

        assertSoftly(softly -> {
            softly.assertThat(orderResponse.getOrderId()).isEqualTo(orderId);
            softly.assertThat(orderResponse.getOrderProducts().toString()).contains(orderProductResponse1.toString(), orderProductResponse2.toString());
            softly.assertThat(orderResponse.getOrderTotalPrice()).isEqualTo(102900);
            softly.assertThat(orderResponse.getUsedPoint()).isEqualTo(100);
        });
    }

    @Test
    void 잘못된_사용자_정보로_주문_상품_상세정보_요청시_실패한다() {
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        ExtractableResponse<Response> orderProductsResponse = requestPostOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItemsDetail(illegalMember, orderId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 잘못된_주문내역_아이디로_주문_상품_상세정보_요청시_실패한다() {
        ExtractableResponse<Response> orderProductsResponse = requestPostOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItemsDetail(member, orderId + 10L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> requestGetOrderItemsDetail(Member member, long orderId) {
        return given()
                .log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestGetOrderItems(Member member) {
        return given()
                .log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestPostOrderProducts(Member member, List<Long> orderItems, int usedPoints) {
        OrderProductsRequest orderProductsRequest = new OrderProductsRequest(orderItems, usedPoints, DELIVERY_FEE);
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderProductsRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
