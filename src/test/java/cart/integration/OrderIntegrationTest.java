package cart.integration;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.order.OrderProductsRequest;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CartItemDao cartItemDao;

    private static final int USED_POINT = 100;

    private Member member;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private List<String> orderItems;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);

        cartItem1 = cartItemDao.findById(1L);
        cartItem2 = cartItemDao.findById(2L);

        orderItems = List.of(cartItem1.getProduct().getName(), cartItem2.getProduct().getName());
    }


    @Test
    void 사용자는_장바구니에_담긴_상품을_주문할_수_있다() {
        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, USED_POINT);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getIdFromCreatedResponse(response)).isGreaterThan(0);
    }

    @Test
    void 잘못된_사용자_정보로_주문_요청시_실패한다() {
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");

        ExtractableResponse<Response> response = requestOrderProducts(illegalMember, orderItems, USED_POINT);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 장바구니에_없는_상품에_대한_주문_요청시_실패한다() {
        List<String> illegalOrderItems = List.of(cartItem1.getProduct().getName() + "asdf", cartItem2.getProduct().getName() + "asdf");

        ExtractableResponse<Response> response = requestOrderProducts(member, illegalOrderItems, USED_POINT);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문_요청시_포인트가_음수인_경우_실패한다() {
        int illegalUsedPoint = -1;

        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, illegalUsedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 주문_요청시_포인트가_사용자의_소유_포인트보다_많은_경우_실패한다() {
        int illegalUsedPoint = member.getPoint() + 100;

        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, illegalUsedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 사용자의_주문_상품을_조회한다() {
        ExtractableResponse<Response> orderProductsResponse = requestOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItems(member);

        assertThat(response.body().jsonPath())
                .hasFieldOrPropertyWithValue("orderId", orderId)
                .hasFieldOrPropertyWithValue("orderProducts", List.of(cartItem1, cartItem2))
                .hasFieldOrProperty("productId")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("imageUrl")
                .hasFieldOrProperty("quantity")
                .hasFieldOrProperty("price")
                .hasFieldOrProperty("totalPrice");
    }

    @Test
    void 사용자의_주문_상품_상세정보를_조회한다() {
        ExtractableResponse<Response> orderProductsResponse = requestOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItemsDetail(member, orderId);

        assertThat(response.body().jsonPath())
                .hasFieldOrPropertyWithValue("orderId", orderId)
                .hasFieldOrPropertyWithValue("orderProducts", List.of(cartItem1, cartItem2))
                .hasFieldOrProperty("productId")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("imageUrl")
                .hasFieldOrProperty("quantity")
                .hasFieldOrProperty("price")
                .hasFieldOrProperty("totalPrice")
                .hasFieldOrProperty("orderTotalPrice")
                .hasFieldOrProperty("usedPoint")
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void 잘못된_사용자_정보로_주문_상품_상세정보_요청시_실패한다() {
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        ExtractableResponse<Response> orderProductsResponse = requestOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItemsDetail(illegalMember, orderId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 잘못된_주문내역_아이디로_주문_상품_상세정보_요청시_실패한다() {
        ExtractableResponse<Response> orderProductsResponse = requestOrderProducts(member, orderItems, USED_POINT);
        long orderId = getIdFromCreatedResponse(orderProductsResponse);

        ExtractableResponse<Response> response = requestGetOrderItemsDetail(member, orderId + 10L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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

    private ExtractableResponse<Response> requestOrderProducts(Member member, List<String> orderItems, int usedPoints) {
        OrderProductsRequest orderProductsRequest = new OrderProductsRequest(orderItems, usedPoints);
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
