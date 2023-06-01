package common;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.CartItemRequest;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class Steps {

    public static ExtractableResponse<Response> 상품을_장바구니에_담는다(CartItem cartItem) {
        final Member member = cartItem.getMember();

        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, TestUtils.toBasicAuthHeaderValue(getAuthValue(member)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(TestUtils.toJson(new CartItemRequest(cartItem.getProduct().getId())))
                .when()
                .post("/cart-items")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장바구니의_상품을_주문한다(Order order, long paymentAmount) {
        final List<CartItem> cartItems = order.getCartItems();
        final Member member = cartItems.get(0).getMember();
        final List<Long> cartItemIds = cartItems
                .stream()
                .map(CartItem::getId)
                .collect(Collectors.toUnmodifiableList());

        return given().log().all()
                .header(HttpHeaders.AUTHORIZATION, TestUtils.toBasicAuthHeaderValue(getAuthValue(member)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(TestUtils.toJson(new OrderRequest(cartItemIds, paymentAmount)))
                .when()
                .post("/orders")
                .then().log().all()
                .extract();
    }

    private static String getAuthValue(Member member) {
        return member.getEmail() + ":" + member.getPassword();
    }
}
