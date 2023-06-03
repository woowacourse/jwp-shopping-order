package cart.acceptance;

import cart.domain.Member;
import cart.dto.request.CartItemCreateRequest;
import cart.dto.request.PayRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cart.fixture.MemberFixtures.MEMBER_GITCHAN;
import static cart.fixture.MemberFixtures.MEMBER_IRENE;
import static cart.fixture.ProductFixtures.PRODUCT_REQUEST_AIRPOD;
import static cart.fixture.ProductFixtures.PRODUCT_REQUEST_CAMERA_EOS_M200;
import static cart.steps.CartItemSteps.카트에_아이템_추가하고_아이디_반환;
import static cart.steps.OrderSteps.멤버의_주문_목록_조회_요청;
import static cart.steps.OrderSteps.멤버의_특정_주문을_조회_요청;
import static cart.steps.PaySteps.카트_아이템_주문하고_주문_히스토리_아이디_반환;
import static cart.steps.ProductSteps.제품_추가하고_아이디_반환;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class OrderAcceptanceTest extends AcceptanceTest {

    private static final int JOIN_EVENT_POINT = 5000;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void 멤버의_주문_목록을_조회할_수_있다() {
        final Member savedMember = memberRepository.addMember(MEMBER_GITCHAN, JOIN_EVENT_POINT);
        final Long product1Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
        final Long product2Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_AIRPOD);
        final Long cartItem1Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product1Id, 1));
        final Long cartItem2Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product2Id, 2));

        final int originalPrice = 1_543_490;
        final int usedPoint = 1000;
        final long orderHistoryId = 카트_아이템_주문하고_주문_히스토리_아이디_반환(
                savedMember,
                new PayRequest(List.of(cartItem1Id, cartItem2Id), originalPrice, usedPoint)
        );

        final ExtractableResponse<Response> response = 멤버의_주문_목록_조회_요청(savedMember);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getList("orderId", Long.class).get(0)).isEqualTo(orderHistoryId),
                () -> assertThat(response.jsonPath().getList("orderPrice", Integer.class).get(0)).isEqualTo(originalPrice - usedPoint),
                () -> assertThat(response.jsonPath().getList("totalAmount", Integer.class).get(0)).isEqualTo(2),
                () -> assertThat(response.jsonPath().getList("previewName", String.class).get(0)).isNotBlank()
        );
    }

//    @Test
//    void 특정_주문을_하지_않은_사용자는_해당_특정_주문_상세_페이지에_접근할_수_없다() {
//        final Member orderMember = memberRepository.addMember(MEMBER_GITCHAN, JOIN_EVENT_POINT);
//        final Member otherMember = memberRepository.addMember(MEMBER_IRENE, JOIN_EVENT_POINT);
//        final Long product1Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
//        final Long product2Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_AIRPOD);
//        final Long cartItem1Id = 카트에_아이템_추가하고_아이디_반환(orderMember, new CartItemCreateRequest(product1Id, 1));
//        final Long cartItem2Id = 카트에_아이템_추가하고_아이디_반환(orderMember, new CartItemCreateRequest(product2Id, 2));
//        final long orderHistoryId = 카트_아이템_주문하고_주문_히스토리_아이디_반환(
//                orderMember,
//                new PayRequest(List.of(cartItem1Id, cartItem2Id), 1_543_490, 1000)
//        );
//
//        final ExtractableResponse<Response> response = 멤버의_특정_주문을_조회_요청(otherMember, orderHistoryId);
//
//        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
//    }

    @Test
    void 멤버의_특정_주문을_상세하게_조회할_수_있다() {
        final Member savedMember = memberRepository.addMember(MEMBER_GITCHAN, JOIN_EVENT_POINT);
        final Long product1Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
        final Long product2Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_AIRPOD);
        final Long cartItem1Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product1Id, 1));
        final Long cartItem2Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product2Id, 2));

        final long orderHistoryId = 카트_아이템_주문하고_주문_히스토리_아이디_반환(
                savedMember,
                new PayRequest(List.of(cartItem1Id, cartItem2Id), 1_543_490, 1000)
        );

        final ExtractableResponse<Response> response = 멤버의_특정_주문을_조회_요청(savedMember, orderHistoryId);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(
                        response.jsonPath().getList("name")
                                .containsAll(
                                        List.of(
                                                productRepository.getProductById(product1Id).getName(),
                                                productRepository.getProductById(product2Id).getName()
                                        )
                                )
                ).isTrue(),
                () -> assertThat(
                        response.jsonPath().getList("count")
                                .containsAll(
                                        List.of(
                                                cartItemRepository.findById(cartItem1Id).getQuantity(),
                                                cartItemRepository.findById(cartItem2Id).getQuantity()
                                        )
                                )
                ).isTrue()
        );
    }
}
