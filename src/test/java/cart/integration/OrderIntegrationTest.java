package cart.integration;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.dto.CartItemRequest;
import cart.dto.OrderInfo;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;
import io.restassured.RestAssured;
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
import static cart.common.step.CartItemStep.addCartItem;
import static cart.common.step.ProductStep.addProductAndGetId;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @BeforeEach
    void setUp() {
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberDao.addMember(new MemberEntity(EMAIL, PASSWORD, 1000));
        super.setUp();
    }

    @Test
    void 주문한다() {
        //given
        final Long productId = addProductAndGetId(new ProductRequest("chicken", 20000, "chicken.jpeg"));
        addCartItem(EMAIL, PASSWORD, new CartItemRequest(productId));

        final OrderRequest orderRequest = new OrderRequest(List.of(new OrderInfo(productId, 1)), 19000, 1000);        //when

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
}
