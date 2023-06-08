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
        assertAll(
                () -> assertThat(jsonPath.getLong("[0].id")).isEqualTo(order.getId()),
                () -> assertThat(jsonPath.getString("[0].orderedTime")).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderedTime())),

                () -> assertThat(jsonPath.getLong("[0].orderedItems[0].id")).isEqualTo(order.getOrderItems().get(0).getProductId()),
                () -> assertThat(jsonPath.getString("[0].orderedItems[0].name")).isEqualTo(order.getOrderItems().get(0).getProductName()),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[0].price")).isEqualTo(order.getOrderItems().get(0).getOriginalPrice()),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[0].quantity")).isEqualTo(order.getOrderItems().get(0).getQuantity()),
                () -> assertThat(jsonPath.getString("[0].orderedItems[0].imageUrl")).isEqualTo(order.getOrderItems().get(0).getImgUri()),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[0].totalPrice")).isEqualTo(6_000),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[0].totalDiscountPrice")).isEqualTo(4_000),

                () -> assertThat(jsonPath.getLong("[0].orderedItems[1].id")).isEqualTo(order.getOrderItems().get(1).getProductId()),
                () -> assertThat(jsonPath.getString("[0].orderedItems[1].name")).isEqualTo(order.getOrderItems().get(1).getProductName()),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[1].price")).isEqualTo(order.getOrderItems().get(1).getOriginalPrice()),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[1].quantity")).isEqualTo(order.getOrderItems().get(1).getQuantity()),
                () -> assertThat(jsonPath.getString("[0].orderedItems[1].imageUrl")).isEqualTo(order.getOrderItems().get(1).getImgUri()),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[1].totalPrice")).isEqualTo(11700),
                () -> assertThat(jsonPath.getInt("[0].orderedItems[1].totalDiscountPrice")).isEqualTo(1300)
        );
    }

    @Test
    void 상세_주문정보_페이지에_접속한다() {
        super.사용자가_결제하기_버튼을_누른다();
        final var order = orderRepository.findById(1L);
        final var 결과 = given()
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/orders/{id}", 1)
                .then().log().all()
                .extract();

        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var jsonPath = 결과.jsonPath();

        assertAll(
                () -> assertThat(jsonPath.getLong("id")).isEqualTo(order.getId()),
                () -> assertThat(jsonPath.getString("orderedTime")).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderedTime())),

                () -> assertThat(jsonPath.getLong("orderedItems[0].id")).isEqualTo(order.getOrderItems().get(0).getProductId()),
                () -> assertThat(jsonPath.getString("orderedItems[0].name")).isEqualTo(order.getOrderItems().get(0).getProductName()),
                () -> assertThat(jsonPath.getInt("orderedItems[0].price")).isEqualTo(order.getOrderItems().get(0).getOriginalPrice()),
                () -> assertThat(jsonPath.getInt("orderedItems[0].quantity")).isEqualTo(order.getOrderItems().get(0).getQuantity()),
                () -> assertThat(jsonPath.getString("orderedItems[0].imageUrl")).isEqualTo(order.getOrderItems().get(0).getImgUri()),
                () -> assertThat(jsonPath.getInt("orderedItems[0].totalPrice")).isEqualTo(6_000),
                () -> assertThat(jsonPath.getInt("orderedItems[0].totalDiscountPrice")).isEqualTo(4_000),

                () -> assertThat(jsonPath.getLong("orderedItems[1].id")).isEqualTo(order.getOrderItems().get(1).getProductId()),
                () -> assertThat(jsonPath.getString("orderedItems[1].name")).isEqualTo(order.getOrderItems().get(1).getProductName()),
                () -> assertThat(jsonPath.getInt("orderedItems[1].price")).isEqualTo(order.getOrderItems().get(1).getOriginalPrice()),
                () -> assertThat(jsonPath.getInt("orderedItems[1].quantity")).isEqualTo(order.getOrderItems().get(1).getQuantity()),
                () -> assertThat(jsonPath.getString("orderedItems[1].imageUrl")).isEqualTo(order.getOrderItems().get(1).getImgUri()),
                () -> assertThat(jsonPath.getInt("orderedItems[1].totalPrice")).isEqualTo(11_700),
                () -> assertThat(jsonPath.getInt("orderedItems[1].totalDiscountPrice")).isEqualTo(1_300),

                () -> assertThat(jsonPath.getInt("deliveryPrice")).isEqualTo(order.getDeliveryPrice()),
                () -> assertThat(jsonPath.getInt("discountFromTotalPrice")).isEqualTo(0)
        );

    }
}
