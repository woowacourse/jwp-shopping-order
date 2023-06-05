package cart.integration;

import cart.domain.Member.Member;
import cart.dto.CartItemRequest;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static cart.integration.apifixture.ApiFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PointIntegrationTest extends IntegrationTest {


    private final Member member1 = new Member(1L, "a@a.com", "1234");

    @Test
    @DisplayName("사용자의 장바구니의 상품을 주문하고 포인트를 조회한다.")
    void orderCartItemAndCheckPoint() {
        // given
        long productId = addProductAndGetId(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        long productId2 = addProductAndGetId(new ProductRequest("피자", 20_000, "http://example.com/pizza.jpg"));

        long cartId = addCartItemAndGetId(member1, new CartItemRequest(productId));
        long cartId2 = addCartItemAndGetId(member1, new CartItemRequest(productId2));

        // when
        createOrder(List.of(cartId, cartId2), member1, 1000);
        ExtractableResponse<Response> response = showUserPoint(member1);

        // then
        assertThat(response.body().jsonPath().getInt("points")).isEqualTo(9725);
    }

    @Test
    @DisplayName("사용자의 장바구니의 상품을 주문하고 주문에 대한 포인트를 조회한다.")
    void orderCartItemAndCheckOrderPoint() {
        // given
        long productId = addProductAndGetId(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        long productId2 = addProductAndGetId(new ProductRequest("피자", 20_000, "http://example.com/pizza.jpg"));

        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        CartItemRequest cartItemRequest2 = new CartItemRequest(productId2);

        long cartId = addCartItemAndGetId(member1, cartItemRequest);
        long cartId2 = addCartItemAndGetId(member1, cartItemRequest2);


        //when
        long orderId = addOrderAndGetId(List.of(cartId, cartId2), member1, 1000);
        ExtractableResponse<Response> response = showOrderPoint(orderId, member1);

        // then
        assertThat(response.body().jsonPath().getInt("points_saved")).isEqualTo(725);
    }

    @Test
    @DisplayName("상품 구매 가격보다 더 많은 포인트를 사용할 때 오류가 난다.")
    void shouldThrowExceptionWhenUsedPointIsBiggerThanTotalPrice() {
        //given
        long productId = addProductAndGetId(new ProductRequest("치킨", 8000, "http://example.com/chicken.jpg"));

        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        long cartId = addCartItemAndGetId(member1, cartItemRequest);

        // when
        ExtractableResponse<Response> response = createOrder(List.of(cartId), member1, 9000);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("보유한 포인트보다 많은 포인트를 사용할 때 오류가 난다")
    void shouldThrowExceptionWhenUsedPointIsBiggerThanHavingPoint() {
        //given
        long productId = addProductAndGetId(new ProductRequest("치킨", 12000, "http://example.com/chicken.jpg"));
        CartItemRequest cartItemRequest = new CartItemRequest(productId);

        long cartId = addCartItemAndGetId(member1, cartItemRequest);

        // when
        ExtractableResponse<Response> response = createOrder(List.of(cartId), member1, 12000);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("포인트를 마이너스로 사용하려 하면 오류가 난다.")
    void shouldThrowExceptionWhenUsedMinusPoint() {
        //given
        long productId = addProductAndGetId(new ProductRequest("치킨", 10000, "http://example.com/chicken.jpg"));

        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        long cartId = addCartItemAndGetId(member1, cartItemRequest);

        // when
        ExtractableResponse<Response> response = createOrder(List.of(cartId), member1, -100);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
