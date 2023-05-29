package cart.integration;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.dto.OrderRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @DisplayName("장바구니에 들어 있는 상품을 주문할 수 있다.")
    @Test
    void orderTest() {
        final Member member = memberDao.getMemberById(1L);
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1000);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmailValue(), member.getPasswordValue())
                .body(orderRequest).log().all()
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/orders/1");
    }
}
