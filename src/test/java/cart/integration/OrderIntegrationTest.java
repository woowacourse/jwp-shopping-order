package cart.integration;

import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.OrderItemsRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CartItemDao cartItemDao;

    private Member member;

    private CartItem cartItem1;
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);

        cartItem1 = cartItemDao.findById(1L);
        cartItem2 = cartItemDao.findById(2L);
    }


    @Test
    void 사용자는_장바구니에_담긴_상품을_주문할_수_있다() {
        List<String> orderItems = List.of(cartItem1.getProduct().getName(), cartItem2.getProduct().getName());
        int usedPoint = 100;

        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, usedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(getIdFromCreatedResponse(response)).isGreaterThan(0);
    }

    @Test
    void 잘못된_사용자_정보로_주문_요청시_실패한다() {
        Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");

        List<String> orderItems = List.of(cartItem1.getProduct().getName(), cartItem2.getProduct().getName());
        int usedPoint = 100;

        ExtractableResponse<Response> response = requestOrderProducts(illegalMember, orderItems, usedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 장바구니에_없는_상품에_대한_주문_요청시_실패한다() {
        List<String> orderItems = List.of(cartItem1.getProduct().getName() + "asdf", cartItem2.getProduct().getName() + "asdf");
        int usedPoint = 100;

        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, usedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 포인트가_음수인_경우_예외를_던진다() {
        List<String> orderItems = List.of(cartItem1.getProduct().getName(), cartItem2.getProduct().getName());
        int illegalUsedPoint = -1;

        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, illegalUsedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 포인트가_사용자의_소유_포인트보다_많은_경우_예외를_던진다() {
        List<String> orderItems = List.of(cartItem1.getProduct().getName(), cartItem2.getProduct().getName());
        int illegalUsedPoint = member.getPoint() + 100;

        ExtractableResponse<Response> response = requestOrderProducts(member, orderItems, illegalUsedPoint);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> requestOrderProducts(Member member, List<String> orderItems, int usedPoints) {
        OrderItemsRequest orderItemsRequest = new OrderItemsRequest(orderItems, usedPoints);
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderItemsRequest)
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
