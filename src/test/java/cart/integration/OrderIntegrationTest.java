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
import cart.dto.request.OrderCouponRequest;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderProductRequest;
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
public class OrderIntegrationTest extends IntegrationTest{

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
        insertMemberCoupon(MEMBER, 천원_할인_쿠폰, jdbcTemplate);
        List<OrderItemRequest> request = List.of(
                new OrderItemRequest(
                        new OrderProductRequest(1L, "치킨", 10_000, "www.naver.com"),
                        1,
                        List.of(new OrderCouponRequest(천원_할인_쿠폰.getId())),
                        9000
                ),
                new OrderItemRequest(
                        new OrderProductRequest(2L, "피자", 15_000, "www.kakao.com"),
                        1,
                        List.of(),
                        14000
                )
        );

        // when
        ExtractableResponse<Response> response = 주문한다(request, MEMBER);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @Test
    void 주문을_조회한다() {
        // given

        // when

        // then
    }
//
//    @Test
//    void 주문_내역을_조회한다() {
//        // given
//        insertMember(MEMBER, jdbcTemplate);
//        상품_추가("치킨", 10_000, "www.naver.com");
//        상품_추가("피자", 15_000, "www.kakao.com");
//        insertCoupon(천원_할인_쿠폰, jdbcTemplate);
//        insertMemberCoupon(MEMBER, 천원_할인_쿠폰, jdbcTemplate);
//        OrderItemRequest 치킨_주문 = new OrderItemRequest(
//                new OrderProductRequest(1L, "치킨", 10_000, "www.naver.com"),
//                1,
//                List.of(new OrderCouponRequest(천원_할인_쿠폰.getId())),
//                9000
//        );
//        OrderItemRequest 피자_주문 = new OrderItemRequest(
//                new OrderProductRequest(2L, "피자", 15_000, "www.kakao.com"),
//                1,
//                List.of(new OrderCouponRequest(천원_할인_쿠폰.getId())),
//                14000
//        );
//        List<OrderItemRequest> 요청 = List.of(치킨_주문, 피자_주문);
//        long 주문_아이디 = 주문하고_아이디를_반환한다(요청, MEMBER);
//
//        // when
//        ExtractableResponse<Response> response = given().log().all()
//                .auth().preemptive().basic(MEMBER.getEmail(), MEMBER.getPassword())
//                .when()
//                .post("/orders" + "/{id|", 주문_아이디)
//                .then()
//                .log().all()
//                .extract();
//
//        // then
//        OrderResponse body = response.as(
//                new ParameterizedTypeReference<OrderResponse>() {
//                }.getType()
//        );
//
//        assertThat(body.getId()).isPositive();
//        OrderItemResponse 첫번째_주문 = body.getOrderItems().get(0);
//        assertThat(첫번째_주문.getId()).isPositive();
//        assertThat(첫번째_주문.getProduct().getName()).isEqualTo(치킨_주문.getProduct().getName());
//        assertThat(첫번째_주문.getProduct().getPrice()).isEqualTo(치킨_주문.getProduct().getPrice());
//        assertThat(첫번째_주문.getProduct().getImageUrl()).isEqualTo(치킨_주문.getProduct().getImageUrl());
//        assertThat(첫번째_주문.getCoupons().get(0)).isEqualTo(치킨_주문.getCoupons().get(0));
//        assertThat(첫번째_주문.getTotalPrice()).isEqualTo(치킨_주문.getTotalPrice());
//        assertThat(첫번째_주문.getQuantity()).isEqualTo(치킨_주문.getQuantity());
//    }

    private long 주문하고_아이디를_반환한다(List<OrderItemRequest> 요청, Member 회원) {
        ExtractableResponse<Response> response = 주문한다(요청, 회원);
        String location = response.header("location");
        final String id = location.substring(location.lastIndexOf("/") + 1);
        return Long.parseLong(id);
    }

    private ExtractableResponse<Response> 주문한다(List<OrderItemRequest> 요청, Member 회원) {
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
