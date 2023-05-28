package cart.integration;

import cart.dto.OrdersRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.util.List;

import static cart.fixture.Fixture.EMAIL;
import static cart.fixture.Fixture.PASSWORD;
import static org.hamcrest.Matchers.*;


public class OrdersIntegrationTest extends IntegrationTest{

    @Test
    @DisplayName("해당 장바구니를 주문한다")
    void orderTest(){
        final OrdersRequest ordersRequest = new OrdersRequest(List.of(1L,2L,3L),24700,2200,1L);
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ordersRequest)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("location",is("orders/1"));
    }

    // TODO : orders 변수명 확정하기
    @Test
    @DisplayName("주문 목록을 가져온다")
    void getOrderListTest(){
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when().get("/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("orders",hasSize(3));
    }
    @Test
    @DisplayName("주문 목록을 가져온다")
    void getOrderDetailTest(){
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when().get("/orders/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id",equalTo(1L))
                .body("order_products",hasSize(3))
                .body("coupon",hasItems("id", "name", "discount_type", "discount_rate", "discount_amount", "minimum_price"));
    }
    @Test
    @DisplayName("주문 확정을 한다")
    void confirmOrderTest() {
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when().patch("/orders/1/confirm")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("coupon", hasItems("id", "name", "discount_type", "discount_rate", "discount_amount", "minimum_price"));
    }
    @Test
    @DisplayName("주문 취소를 한다")
    void cancelOrder(){
        RestAssured.given().log().all()
                .auth().preemptive().basic(EMAIL, PASSWORD)
                .when().delete("/orders/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
