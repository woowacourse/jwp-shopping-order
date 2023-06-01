package cart.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문화면에서 일어나는 API 시나리오")
public class OrderScenarioTest extends ScenarioFixture {
    @Test
    void 사용자가_장바구니에_담은_상품들을_보여준다() {
        final var response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();

        final var jsonPath = response.jsonPath();

        assertAll(
                "Body에 들어있는 첫번 째 상품 검증하기",
                () -> assertThat(jsonPath.getLong("[0].id")).isEqualTo(치킨장바구니.getId()),
                () -> assertThat(jsonPath.getLong("[0].quantity")).isEqualTo(치킨장바구니.getQuantity()),
                () -> assertThat(jsonPath.getLong("[0].product.id")).isEqualTo(치킨.getId()),
                () -> assertThat(jsonPath.getLong("[0].product.price")).isEqualTo(치킨.getPrice()),
                () -> assertThat(jsonPath.getString("[0].product.name")).isEqualTo(치킨.getName()),
                () -> assertThat(jsonPath.getString("[0].product.imageUrl")).isEqualTo(치킨.getImageUrl()),
                () -> assertThat(jsonPath.getBoolean("[0].product.isOnSale")).isEqualTo(true),
                () -> assertThat(jsonPath.getLong("[0].product.salePrice")).isEqualTo(3000)
        );

        assertAll(
                "Body에 들어있는 두번 째 상품 검증하기",
                () -> assertThat(jsonPath.getLong("[1].id")).isEqualTo(샐러드장바구니.getId()),
                () -> assertThat(jsonPath.getLong("[1].quantity")).isEqualTo(샐러드장바구니.getQuantity()),
                () -> assertThat(jsonPath.getLong("[1].product.id")).isEqualTo(샐러드.getId()),
                () -> assertThat(jsonPath.getLong("[1].product.price")).isEqualTo(샐러드.getPrice()),
                () -> assertThat(jsonPath.getString("[1].product.name")).isEqualTo(샐러드.getName()),
                () -> assertThat(jsonPath.getString("[1].product.imageUrl")).isEqualTo(샐러드.getImageUrl()),
                () -> assertThat(jsonPath.getBoolean("[1].product.isOnSale")).isEqualTo(false),
                () -> assertThat(jsonPath.getLong("[1].product.salePrice")).isEqualTo(0)
        );

        assertAll(
                "Body에 들어있는 세번 째 상품 검증하기",
                () -> assertThat(jsonPath.getLong("[2].id")).isEqualTo(피자장바구니.getId()),
                () -> assertThat(jsonPath.getLong("[2].quantity")).isEqualTo(피자장바구니.getQuantity()),
                () -> assertThat(jsonPath.getLong("[2].product.id")).isEqualTo(피자.getId()),
                () -> assertThat(jsonPath.getLong("[2].product.price")).isEqualTo(피자.getPrice()),
                () -> assertThat(jsonPath.getString("[2].product.name")).isEqualTo(피자.getName()),
                () -> assertThat(jsonPath.getString("[2].product.imageUrl")).isEqualTo(피자.getImageUrl()),
                () -> assertThat(jsonPath.getBoolean("[2].product.isOnSale")).isEqualTo(false),
                () -> assertThat(jsonPath.getLong("[2].product.salePrice")).isEqualTo(0)
        );
    }

    @Test
    void 사용자가_지불해야할_배송비를_보여준다() {
        final var response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .when()
                .get("/delivery-policy")
                .then()
                .log().all()
                .extract();
        final var jsonPath = response.jsonPath();

        assertThat(jsonPath.getLong("price")).isEqualTo(3000);
        assertThat(jsonPath.getLong("limit")).isEqualTo(30000);
    }

    @Test
    void 사용자가_가지고있는_쿠폰들을_보여준다() {
        final var response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .when()
                .get("/coupons")
                .then()
                .log().all()
                .extract();
        final var jsonPath = response.jsonPath();

        assertThat(jsonPath.getLong("[0].id")).isEqualTo(전체10프로할인쿠폰.getId());
        assertThat(jsonPath.getString("[0].name")).isEqualTo(전체10프로할인쿠폰.getName());
        assertThat(jsonPath.getLong("[1].id")).isEqualTo(전체20프로할인쿠폰.getId());
        assertThat(jsonPath.getString("[1].name")).isEqualTo(전체20프로할인쿠폰.getName());
        assertThat(jsonPath.getLong("[2].id")).isEqualTo(배송비무료쿠폰.getId());
        assertThat(jsonPath.getString("[2].name")).isEqualTo(배송비무료쿠폰.getName());
    }

    @Test
    @DisplayName("사용자가 적용할 쿠폰 목록을 체크한다.")
    void 사용자가_적용할_쿠폰_목록을_체크한다() {
        final var 결과 = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .when()
                .get("/cart-items/coupon?id={coupon1},{coupon2}", 전체10프로할인쿠폰.getId(), 배송비무료쿠폰.getId())
                .then().log().all()
                .extract();

        final var jsonPath = 결과.jsonPath();

        assertAll(
                "쿠폰의 할인정책이 적용된 첫 번째 상품 검증",
                () -> assertThat(jsonPath.getLong("cartItemsPrice[0].cartItemId")).isEqualTo(치킨.getId()),
                () -> assertThat(jsonPath.getLong("cartItemsPrice[0].originalPrice")).isEqualTo(치킨.getPrice()),
                () -> assertThat(jsonPath.getLong("cartItemsPrice[0].discountPrice")).isEqualTo(1_000)
        );

        assertAll(
                "쿠폰의 할인정책이 적용된 두 번째 상품 검증",
                () -> assertThat(jsonPath.getLong("cartItemsPrice[1].cartItemId")).isEqualTo(샐러드.getId()),
                () -> assertThat(jsonPath.getLong("cartItemsPrice[1].originalPrice")).isEqualTo(샐러드.getPrice()),
                () -> assertThat(jsonPath.getLong("cartItemsPrice[1].discountPrice")).isEqualTo(2_000)
        );

        assertAll(
                "쿠폰의 할인정책이 적용된 세 번째 상품 검증",
                () -> assertThat(jsonPath.getLong("cartItemsPrice[2].cartItemId")).isEqualTo(피자.getId()),
                () -> assertThat(jsonPath.getLong("cartItemsPrice[2].originalPrice")).isEqualTo(피자.getPrice()),
                () -> assertThat(jsonPath.getLong("cartItemsPrice[2].discountPrice")).isEqualTo(1_300)
        );

        assertAll(
                "쿠폰의 할인정책이 적용된 배송비 검증",
                () -> assertThat(jsonPath.getLong("deliveryPrice.originalPrice")).isEqualTo(3000),
                () -> assertThat(jsonPath.getLong("deliveryPrice.discountPrice")).isEqualTo(3000)
        );

        assertAll(
                "최종 금액에서의 할인액 검증",
                () -> assertThat(jsonPath.getLong("discountFromTotalPrice.discountPrice")).isEqualTo(5000)
        );
    }

    @Test
    void 사용자가_결제하기_버튼을_누른다() {
        final var 요청바디 = Map.of(
                "cartItemIds",
                List.of(치킨.getId(), 피자.getId()),
                "isDeliveryFree", false,
                "totalPaymentPrice", 50000,
                "couponIds",
                List.of(전체10프로할인쿠폰.getId(), 배송비무료쿠폰.getId())
        );
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(사용자1.getEmail(), 사용자1.getPassword())
                .body(요청바디)
                .when()
                .post("/payments")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
