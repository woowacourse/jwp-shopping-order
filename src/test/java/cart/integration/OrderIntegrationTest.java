package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.OrderItemCreateRequest;
import cart.dto.request.OrderCreateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    ProductDao productDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    CartItemDao cartItemDao;

    Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.findById(1L).get();
    }

    @Test
    void 주문을_생성하면_생성_응답을_반환하고_장바구니에_존재하는_물건을_삭제한다() {
        Long productId = productDao.save(new Product(null, "사과", 1000, "http://image.com/image.png"));
        Product product = productDao.findById(productId).get();
        Long cartItemId = cartItemDao.save(new CartItem(null, 5, product, member));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(List.of(
                new OrderItemCreateRequest(productId, 5)
        ), 0L);

        ExtractableResponse<Response> response = given()
                .log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderCreateRequest)
                .when()
                .post("/orders")
                .then()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/orders/1"),
                () -> assertThat(cartItemDao.findById(cartItemId)).isEmpty()
        );
    }
}
