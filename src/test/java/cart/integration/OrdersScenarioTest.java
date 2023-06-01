package cart.integration;

import cart.order.application.OrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.text.SimpleDateFormat;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문조회 화면에서 일어나는 API 시나리오")
public class OrdersScenarioTest extends OrderScenarioTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Disabled
    void 전체_주문내역_페이지에_접속한다() {
        super.사용자가_결제하기_버튼을_누른다();
        final var order = orderRepository.findById(1L);

        final var 결과 = given().log().all()
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/orders")
                .then().log().all()
                .extract();

        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var jsonPath = 결과.jsonPath();
        assertThat(jsonPath.getList("orders")).hasSize(1);
        assertAll(
                () -> assertThat(jsonPath.getLong("orders[0].id")).isEqualTo(order.getId()),
                () -> assertThat(jsonPath.getString("orders[0].orderedTime")).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderedTime())),

                () -> assertThat(jsonPath.getLong("orders[0].orderedItems[0].id")).isEqualTo(order.getOrderItems().get(0).getProductId()),
                () -> assertThat(jsonPath.getString("orders[0].orderedItems[0].name")).isEqualTo(order.getOrderItems().get(0).getProductName()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[0].price")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[0].quantity")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[0].imageUrl")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[0].totalPrice")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[0].totalDiscountPrice")).isEqualTo(order.getOrderItems().get(0).getPrice()),

                () -> assertThat(jsonPath.getLong("orders[0].orderedItems[1].id")).isEqualTo(order.getOrderItems().get(0).getProductId()),
                () -> assertThat(jsonPath.getString("orders[0].orderedItems[1].name")).isEqualTo(order.getOrderItems().get(0).getProductName()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[1].price")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[1].quantity")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[1].imageUrl")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[1].totalPrice")).isEqualTo(order.getOrderItems().get(0).getPrice()),
                () -> assertThat(jsonPath.getInt("orders[0].orderedItems[1].totalDiscountPrice")).isEqualTo(order.getOrderItems().get(0).getPrice())
        );
    }

    @Test
    @Disabled
    void 상세_주문정보_페이지에_접속한다() {
        super.사용자가_결제하기_버튼을_누른다();
        final var order = orderRepository.findById(1L);
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
                () -> assertThat(jsonPath.getLong("orderId")).isEqualTo(order.getId()),
                () -> assertThat(jsonPath.getString("orderedTime")).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderedTime())),

                () -> assertThat(jsonPath.getLong("products[0].productId")).isEqualTo(order.getOrderItems().get(0).getProductId()),
                () -> assertThat(jsonPath.getString("products[0].productName")).isEqualTo(order.getOrderItems().get(0).getProductName()),
                () -> assertThat(jsonPath.getInt("products[0].price")).isEqualTo(order.getOrderItems().get(0).getPrice()),

                () -> assertThat(jsonPath.getLong("products[1].productId")).isEqualTo(order.getOrderItems().get(1).getProductId()),
                () -> assertThat(jsonPath.getString("products[1].productName")).isEqualTo(order.getOrderItems().get(1).getProductName()),
                () -> assertThat(jsonPath.getInt("products[1].price")).isEqualTo(order.getOrderItems().get(1).getPrice()),

                () -> assertThat(jsonPath.getInt("deliveryPrice.price")).isEqualTo(order.getDeliveryPrice()),

                () -> assertThat(jsonPath.getLong("coupons[0].couponId")).isEqualTo(order.getOrderCoupons().get(0).getCouponId()),
                () -> assertThat(jsonPath.getString("coupons[0].couponName")).isEqualTo(order.getOrderCoupons().get(0).getCouponName()),

                () -> assertThat(jsonPath.getLong("coupons[1].couponId")).isEqualTo(order.getOrderCoupons().get(1).getCouponId()),
                () -> assertThat(jsonPath.getString("coupons[1].couponName")).isEqualTo(order.getOrderCoupons().get(1).getCouponName())
        );
    }
}
