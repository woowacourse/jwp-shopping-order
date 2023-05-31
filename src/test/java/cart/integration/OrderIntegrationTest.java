package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemOrderRequest;
import cart.dto.CartItemRequest;
import cart.dto.OrderRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

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
}
