package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemOrderRequest;
import cart.dto.OrderRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;
    
    private Member member;
    
    @BeforeEach
    void setUp() {
        super.setUp();
        
        member = memberDao.getMemberById(1L);
    }
    
    @Test
    void 장바구니_목록을_주문한다() {
        // given
        final OrderRequest orderRequest =
                new OrderRequest(List.of(new CartItemOrderRequest(1L)), 20000L, 5000L, 2000L);
        
        // expect
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        
        final String orderId = response.header("Location").substring("/orders/".length());
        
        assertThat(Long.parseLong(orderId)).isPositive();
    }
    
    @Test
    void 주문_목록을_조회한다() {
        // given
        final OrderRequest orderRequest1 =
                new OrderRequest(List.of(new CartItemOrderRequest(1L), new CartItemOrderRequest(2L)), 100000L, 5000L, 10000L);
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        final OrderRequest orderRequest2 =
                new OrderRequest(List.of(new CartItemOrderRequest(4L)), 65000L, 5000L, 6500L);
        
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest2)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        
        // expect
        given().log().all()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
        
    }
}
