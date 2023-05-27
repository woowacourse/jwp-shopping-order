package cart.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문조회 화면에서 일어나는 API 시나리오")
public class OrdersScenarioTest extends ScenarioFixture {
    @Test
    void 전체_주문내역_페이지에_접속한다() {
        final var 결과 = given()
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/orders")
                .then()
                .extract();

        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var jsonPath = 결과.jsonPath();
        assertThat(jsonPath.getList("orders")).hasSize(2);
        assertAll(
                () -> assertThat(jsonPath.getLong("orders[0].orderId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("orders[0].orderedTime")).isEqualTo("2023-05-25"),

                () -> assertThat(jsonPath.getLong("orders[0].products[0].productId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("orders[0].products[0].productName")).isEqualTo("아이스 아메리카노"),
                () -> assertThat(jsonPath.getLong("orders[0].products[0].price")).isEqualTo(900),

                () -> assertThat(jsonPath.getLong("orders[0].products[1].productId")).isEqualTo(2),
                () -> assertThat(jsonPath.getString("orders[0].products[1].productName")).isEqualTo("라떼"),
                () -> assertThat(jsonPath.getLong("orders[0].products[1].price")).isEqualTo(2700),

                () -> assertThat(jsonPath.getLong("orders[0].deliveryPrice.price")).isEqualTo(0),

                () -> assertThat(jsonPath.getLong("orders[0].coupons[0].couponId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("orders[0].coupons[0].couponName")).isEqualTo("10% 전체 할인 쿠폰"),

                () -> assertThat(jsonPath.getLong("orders[0].coupons[1].couponId")).isEqualTo(3),
                () -> assertThat(jsonPath.getString("orders[0].coupons[1].couponName")).isEqualTo("배송비 무료 쿠폰")
        );

        assertAll(
                () -> assertThat(jsonPath.getLong("orders[1].orderId")).isEqualTo(2),
                () -> assertThat(jsonPath.getString("orders[1].orderedTime")).isEqualTo("2021-03-15"),

                () -> assertThat(jsonPath.getLong("orders[1].products[0].productId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("orders[1].products[0].productName")).isEqualTo("아이스 아메리카노"),
                () -> assertThat(jsonPath.getLong("orders[1].products[0].price")).isEqualTo(1000),

                () -> assertThat(jsonPath.getLong("orders[1].products[1].productId")).isEqualTo(2),
                () -> assertThat(jsonPath.getString("orders[1].products[1].productName")).isEqualTo("라떼"),
                () -> assertThat(jsonPath.getLong("orders[1].products[1].price")).isEqualTo(3000),

                () -> assertThat(jsonPath.getLong("orders[1].deliveryPrice.price")).isEqualTo(0),

                () -> assertThat(jsonPath.getLong("orders[1].coupons[0].couponId")).isEqualTo(3),
                () -> assertThat(jsonPath.getString("orders[1].coupons[0].couponName")).isEqualTo("배송비 무료 쿠폰")
        );
    }

    @Test
    void 상세_주문정보_페이지에_접속한다() {
        final var 결과 = given()
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/orders/{id}", 1)
                .then()
                .extract();

        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var jsonPath = 결과.jsonPath();

        assertAll(
                () -> assertThat(jsonPath.getLong("$.orderId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("$.orderedTime")).isEqualTo("2023-05-25"),

                () -> assertThat(jsonPath.getLong("$.products[0].productId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("$.products[0].productName")).isEqualTo("아이스 아메리카노"),
                () -> assertThat(jsonPath.getLong("$.products[0].price")).isEqualTo(900),

                () -> assertThat(jsonPath.getLong("$.products[1].productId")).isEqualTo(2),
                () -> assertThat(jsonPath.getString("$.products[1].productName")).isEqualTo("라떼"),
                () -> assertThat(jsonPath.getLong("$.products[1].price")).isEqualTo(2700),

                () -> assertThat(jsonPath.getLong("$.deliveryPrice.price")).isEqualTo(0),

                () -> assertThat(jsonPath.getLong("$.coupons[0].couponId")).isEqualTo(1),
                () -> assertThat(jsonPath.getString("$.coupons[0].couponName")).isEqualTo("10% 전체 할인 쿠폰"),

                () -> assertThat(jsonPath.getLong("$.coupons[1].couponId")).isEqualTo(3),
                () -> assertThat(jsonPath.getString("$.coupons[1].couponName")).isEqualTo("배송비 무료 쿠폰")
        );
    }
}
