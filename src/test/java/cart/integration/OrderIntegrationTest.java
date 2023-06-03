package cart.integration;


import static cart.fixture.CouponFixture.천원_할인_쿠폰;
import static cart.fixture.JdbcTemplateFixture.insertCoupon;
import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertMemberCoupon;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.integration.ProductIntegrationTest.상품_추가;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.request.DiscountRequest;
import cart.dto.request.OrderCouponRequest;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderProductRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderItemResponse;
import cart.dto.response.OrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderApiController 통합 테스트은(는)")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 주문에_성공한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        상품_추가("치킨", 10_000, "www.naver.com");
        상품_추가("피자", 15_000, "www.kakao.com");
        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
        MemberCoupon 쿠폰원 = new MemberCoupon(1L, MEMBER.getId(), 천원_할인_쿠폰, false);
        insertMemberCoupon(쿠폰원, jdbcTemplate);
        OrderRequest request =
                new OrderRequest(3000,
                        List.of(
                                new OrderItemRequest(
                                        new OrderProductRequest(1L, "치킨", 10_000, "www.naver.com"),
                                        1,
                                        List.of()
                                ),
                                new OrderItemRequest(
                                        new OrderProductRequest(2L, "피자", 15_000, "www.kakao.com"),
                                        1,
                                        List.of(new OrderCouponRequest(쿠폰원.getId(), 쿠폰원.getCoupon().getName(),
                                                new DiscountRequest(쿠폰원.getCoupon().getType().name(),
                                                        쿠폰원.getCoupon().getDiscountAmount())))
                                )
                        ));

        // when
        ExtractableResponse<Response> response = 주문한다(request, MEMBER);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 단일_주문_내역을_조회한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        상품_추가("치킨", 10_000, "www.naver.com");
        상품_추가("피자", 15_000, "www.kakao.com");
        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
        MemberCoupon 쿠폰원 = new MemberCoupon(1L, MEMBER.getId(), 천원_할인_쿠폰, false);
        insertMemberCoupon(쿠폰원, jdbcTemplate);
        OrderItemRequest 치킨_주문 = new OrderItemRequest(
                new OrderProductRequest(1L, "치킨", 10_000, "www.naver.com"),
                1,
                List.of()
        );
        OrderItemRequest 피자_주문 = new OrderItemRequest(
                new OrderProductRequest(2L, "피자", 15_000, "www.kakao.com"),
                1,
                List.of(new OrderCouponRequest(쿠폰원.getId(), 쿠폰원.getCoupon().getName(),
                        new DiscountRequest(쿠폰원.getCoupon().getType().name(),
                                쿠폰원.getCoupon().getDiscountAmount())))
        );

        OrderRequest 요청 = new OrderRequest(3000, List.of(치킨_주문, 피자_주문));

        주문한다(요청, MEMBER);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .auth().preemptive().basic(MEMBER.getEmail(), MEMBER.getPassword())
                .when()
                .get("/orders/{id}", 1L)
                .then()
                .log().all()
                .extract();

        // then
        OrderResponse body = response.as(OrderResponse.class);

        assertThat(body.getId()).isPositive();
        OrderItemResponse 첫번째_주문 = body.getOrderItems().get(0);
        assertThat(첫번째_주문.getId()).isPositive();
        assertThat(첫번째_주문.getProduct().getName()).isEqualTo(치킨_주문.getProduct().getName());
        assertThat(첫번째_주문.getProduct().getPrice()).isEqualTo(치킨_주문.getProduct().getPrice());
        assertThat(첫번째_주문.getProduct().getImageUrl()).isEqualTo(치킨_주문.getProduct().getImageUrl());
        assertThat(첫번째_주문.getCoupons()).isEmpty();
        assertThat(첫번째_주문.getTotalPrice()).isPositive();
        assertThat(첫번째_주문.getQuantity()).isEqualTo(치킨_주문.getQuantity());
    }

    //TODO
    @Test
    void 모든_주문_내역을_조회한다() {
        // given

        // when

        // then
    }

    private ExtractableResponse<Response> 주문한다(OrderRequest 요청, Member 회원) {
        return given().log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .contentType(JSON)
                .body(요청)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }
}
