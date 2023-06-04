package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import cart.dao.PointDao;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.entity.PointEntity;
import cart.exception.NotEnoughStockException;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertProduct.sql", "classpath:insertCartItem.sql", "classpath:insertPoint.sql"})
class OrderRollbackTest extends IntegrationTest {

    @Autowired
    private PointDao pointDao;

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = new Member(2L, "odo2@woowa.com", "1234");
    }

    @DisplayName("재고 소진으로 주문 실패 시 포인트는 롤백이 된다.")
    @Test
    void shouldRollbackWhenOrderFail() {
        final OrderRequest orderRequest = new OrderRequest(List.of(6L, 7L), 100, 205_000);
        final ExtractableResponse<Response> response = requestCreateOrder(member, orderRequest);
        final JsonPath result = response.jsonPath();
        final List<PointEntity> pointEntities = pointDao.findRemainingPointsByMemberId(member.getId());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value()),
                () -> assertThat(result.getInt("errorCode")).isEqualTo(1),
                () -> assertThat(result.getString("message")).isEqualTo(new NotEnoughStockException(10, 11).getMessage()),
                () -> assertThat(pointEntities).hasSize(1),
                () -> assertThat(pointEntities.get(0).getEarnedPoint()).isEqualTo(400),
                () -> assertThat(pointEntities.get(0).getLeftPoint()).isEqualTo(200)
        );
    }

    private ExtractableResponse<Response> requestCreateOrder(final Member member, final OrderRequest orderRequest) {
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
}
