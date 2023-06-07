package cart.integration;

import cart.domain.Member;
import cart.dto.OrderRequest;
import cart.dto.ProductOrderRequest;
import cart.repository.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

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

        member = memberRepository.getMemberById(1L);
        member2 = memberRepository.getMemberById(2L);
    }

    @DisplayName("상품을 주문할 수 있다.")
    @Test
    void orderItems() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        ExtractableResponse<Response> response1 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response2 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest2)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        ExtractableResponse<Response> response3 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .body(orderRequest3)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response1.statusCode()).isEqualTo(201),
                () -> assertThat(response2.statusCode()).isEqualTo(201),
                () -> assertThat(response3.statusCode()).isEqualTo(201)
        );
    }

    @DisplayName("등록되지 않은 사용자는 상품을 주문할 수 없다.")
    @Test
    void orderItems_fail1() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        ExtractableResponse<Response> response1 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic("kong", "1234")
                .body(orderRequest1)
                .log().all()
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response1.statusCode()).isEqualTo(400);
    }

    @DisplayName("없는 상품을 전달한 경우에는 상품을 주문할 수 없다.")
    @Test
    void orderItems_fail2() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(100L, 1), new ProductOrderRequest(2L, 1)));

        ExtractableResponse<Response> response1 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response1.statusCode()).isEqualTo(400);
    }

    @DisplayName("가지고 있는 포인트보다 더 많은 포인트를 사용하는 경우에는 상품을 주문할 수 없다.")
    @Test
    void orderItems_fail3() {
        OrderRequest orderRequest1 = new OrderRequest(2000, List.of(new ProductOrderRequest(2L, 1), new ProductOrderRequest(2L, 1)));

        ExtractableResponse<Response> response1 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest1)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        assertThat(response1.statusCode()).isEqualTo(400);
    }

    @DisplayName("상품 목록을 확인할 수 있다. - 10개 이내")
    @Test
    void findAllOrders_1() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response4 = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders?page=1")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response4.statusCode()).isEqualTo(200),
                () -> assertThat(response4.body().jsonPath().getList("contents").size()).isEqualTo(2)
        );
    }

    @DisplayName("상품 목록을 확인할 수 있다. - 10개 이상")
    @Test
    void findAllOrders_2() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest4 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest5 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest6 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest7 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest8 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest9 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest10 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest11 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest12 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member, orderRequest3);
        postOrder(member, orderRequest4);
        postOrder(member, orderRequest5);
        postOrder(member, orderRequest6);
        postOrder(member, orderRequest7);
        postOrder(member, orderRequest8);
        postOrder(member, orderRequest9);
        postOrder(member, orderRequest10);
        postOrder(member, orderRequest11);
        postOrder(member, orderRequest12);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders?page=1")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.body().jsonPath().getList("contents").size()).isEqualTo(10)
        );
    }

    @DisplayName("상품 목록을 확인할 수 있다. - 10개 이상, 다음 페이지")
    @Test
    void findAllOrders_3() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest4 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest5 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest6 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest7 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest8 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest9 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest10 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest11 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest12 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member, orderRequest3);
        postOrder(member, orderRequest4);
        postOrder(member, orderRequest5);
        postOrder(member, orderRequest6);
        postOrder(member, orderRequest7);
        postOrder(member, orderRequest8);
        postOrder(member, orderRequest9);
        postOrder(member, orderRequest10);
        postOrder(member, orderRequest11);
        postOrder(member, orderRequest12);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders?page=2")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.body().jsonPath().getList("contents").size()).isEqualTo(2)
        );
    }

    @DisplayName("상세 목록을 조회할 수 있다.")
    @Test
    void findOrders() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/1")
                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(response.body().jsonPath().getString("orderId")).isEqualTo("1")
        );
    }

    @DisplayName("없는 주문 번호의 상세 목록을 조회할 수 없다.")
    @Test
    void findOrders_fail1() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/100")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @DisplayName("다른 사람이 주문한 주문 번호의 상세 목록을 조회할 수 없다.")
    @Test
    void findOrders_fail2() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .when()
                .get("/orders/1")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @DisplayName("주문을 취소할 수 있다.")
    @Test
    void deleteOrder() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/orders/1")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(200);
    }


    @DisplayName("없는 주문을 취소할 수 없다.")
    @Test
    void deleteOrder_fail1() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/orders/100")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(400);

    }

    @DisplayName("다른 사람의 주문을 취소할 수 없다.")
    @Test
    void deleteOrder_fail2() {
        OrderRequest orderRequest1 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 10), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest2 = new OrderRequest(100, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));
        OrderRequest orderRequest3 = new OrderRequest(0, List.of(new ProductOrderRequest(1L, 1), new ProductOrderRequest(2L, 1)));

        postOrder(member, orderRequest1);
        postOrder(member, orderRequest2);
        postOrder(member2, orderRequest3);


        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member2.getEmail(), member2.getPassword())
                .when()
                .delete("/orders/1")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(400);
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
