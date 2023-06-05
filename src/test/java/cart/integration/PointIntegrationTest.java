package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.ProductOrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PointIntegrationTest extends IntegrationTest{

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        jdbcTemplate.update("insert into product (name, price, image_url) values ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.update("insert into member(email, password) values('a@a.com', '1234')");
        jdbcTemplate.update("insert into member(email, password) values('b@b.com', '1234')");

        jdbcTemplate.update("insert into cart_item(member_id, product_id, quantity) values (1, 1, 2)");
        jdbcTemplate.update("insert into cart_item(member_id, product_id, quantity) values (1, 2, 4)");
        jdbcTemplate.update("insert into cart_item(member_id, product_id, quantity) values (2, 3, 5)");

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    @DisplayName("주문 후 포인트를 확인할 수 있다. - 포인트를 사용하지 않는 경우")
    @Test
    void getPoints() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member, orderRequest3);

        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .log().all()
                .extract();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.body().jsonPath().getInt("currentPoint")).isEqualTo(4500)
        );
    }

    @DisplayName("주문 후 포인트를 확인할 수 있다. - 포인트를 사용하는 경우")
    @Test
    void getPoints2() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest4 = new OrderRequest(1500, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest5 = new OrderRequest(2000, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member, orderRequest3);
        postOrder(member, orderRequest4);
        postOrder(member, orderRequest5);

        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .log().all()
                .extract();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.body().jsonPath().getInt("currentPoint")).isEqualTo(3825)
        );
    }

    private void postOrder(Member member, OrderRequest orderRequest1) {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }
}
