package com.woowahan.techcourse.order.acceptance;

import static com.woowahan.techcourse.common.acceptance.CommonSteps.비정상_요청_요청한_리소스_찾을_수_없음;
import static com.woowahan.techcourse.common.acceptance.CommonSteps.요청_결과의_상태를_검증한다;
import static com.woowahan.techcourse.common.acceptance.CommonSteps.정상_생성;
import static com.woowahan.techcourse.common.acceptance.CommonSteps.정상_요청;
import static com.woowahan.techcourse.order.acceptance.OrderStep.전체_조회_응답의_사이즈는_N이다;
import static com.woowahan.techcourse.order.acceptance.OrderStep.주문_생성_결과에서_주문_아이디_추출;
import static com.woowahan.techcourse.order.acceptance.OrderStep.주문_생성_요청1;
import static com.woowahan.techcourse.order.acceptance.OrderStep.주문_전체_조회_결과_추출;
import static com.woowahan.techcourse.order.acceptance.OrderStep.주문_전체_조회_요청;
import static com.woowahan.techcourse.order.acceptance.OrderStep.주문_조회_ID로_요청;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowahan.techcourse.cart.dto.CartItemResponse;
import com.woowahan.techcourse.common.acceptance.IntegrationTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SuppressWarnings("NonAsciiCharacters")
@Sql(value = "classpath:/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderAcceptanceTest extends IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setDummyData() {
        jdbcTemplate.execute(
                "INSERT INTO product (id, name, price, image_url) VALUES (1, '치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.execute(
                "INSERT INTO product (id, name, price, image_url) VALUES (2, '샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.execute(
                "INSERT INTO product (id, name, price, image_url) VALUES (3, '피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");

        jdbcTemplate.execute("INSERT INTO member (id, email, password) VALUES (1, 'a@a.com', '1234')");
        jdbcTemplate.execute("INSERT INTO member (id, email, password) VALUES (2, 'b@b.com', '1234')");

        jdbcTemplate.execute("INSERT INTO cart_item (member_id, product_id, quantity) VALUES (1, 1, 5)");

        jdbcTemplate.execute("INSERT INTO amount_discount (id, rate) VALUES (1, 10)");
        jdbcTemplate.execute(
                "INSERT INTO discount_type (id, discount_type, discount_amount_id) VALUES (1, 'PERCENT', 1)");
        jdbcTemplate.execute("INSERT INTO discount_condition (id, discount_condition_type) VALUES (1, 'ALWAYS')");
        jdbcTemplate.execute(
                "INSERT INTO coupon(id, name, discount_type_id, discount_condition_id) VALUES (1, '10% 할인 쿠폰', 1, 1)");
        jdbcTemplate.execute("INSERT INTO coupon_member(id, coupon_id, member_id) VALUES (1, 1, 1)");
    }

    @Test
    void 주문이_잘_생성된다() {
        var 사용자_email = "a@a.com";
        var 사용자_password = "1234";
        var 주문_생성_결과 = 주문_생성_요청1(사용자_email, 사용자_password);

        요청_결과의_상태를_검증한다(주문_생성_결과, 정상_생성);
        var 주문_id = 주문_생성_결과에서_주문_아이디_추출(주문_생성_결과);

        var 전체_조회_결과 = 주문_전체_조회_요청(사용자_email, 사용자_password);
        요청_결과의_상태를_검증한다(전체_조회_결과, 정상_요청);
        var 전체_조회_응답 = 주문_전체_조회_결과_추출(전체_조회_결과);
        전체_조회_응답의_사이즈는_N이다(전체_조회_응답, 1);

        var 주문_응답 = 주문_조회_ID로_요청(주문_id, 사용자_email, 사용자_password);
        요청_결과의_상태를_검증한다(주문_응답, 정상_요청);

        var 없는_응답 = 주문_조회_ID로_요청(주문_id + 1, 사용자_email, 사용자_password);
        요청_결과의_상태를_검증한다(없는_응답, 비정상_요청_요청한_리소스_찾을_수_없음);
    }

    @Test
    void 주문을_하면_카트에서_제거된다() {
        var 사용자_email = "a@a.com";
        var 사용자_password = "1234";

        var 주문_생성_결과 = 주문_생성_요청1(사용자_email, 사용자_password);

        var response = requestGetCartItems(사용자_email, 사용자_password);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultCartItemIds).isEmpty();
    }

    private ExtractableResponse<Response> requestGetCartItems(String email, String password) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(email, password)
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }
}
