package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.repository.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L);
        final int totalPrice = 95_000;
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(cartItemIds, totalPrice);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderCreateRequest)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        final String locationHeader = response.header("Location");

        // then
        assertThat(locationHeader).isNotNull();
    }

}
