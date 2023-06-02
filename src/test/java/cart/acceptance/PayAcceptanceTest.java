package cart.acceptance;

import cart.domain.Member;
import cart.dto.request.CartItemCreateRequest;
import cart.dto.request.PayRequest;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cart.fixture.MemberFixtures.MEMBER_GITCHAN;
import static cart.fixture.ProductFixtures.PRODUCT_REQUEST_AIRPOD;
import static cart.fixture.ProductFixtures.PRODUCT_REQUEST_CAMERA_EOS_M200;
import static cart.steps.CartItemSteps.카트에_아이템_추가하고_아이디_반환;
import static cart.steps.PaySteps.카트_아이템_주문_요청;
import static cart.steps.ProductSteps.제품_추가하고_아이디_반환;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class PayAcceptanceTest extends AcceptanceTest {

    private static final int JOIN_EVENT_POINT = 5000;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 카트에_담긴_아이템을_주문할_수_있다() {
        final Member savedMember = memberRepository.addMember(MEMBER_GITCHAN, JOIN_EVENT_POINT);
        final Long product1Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
        final Long product2Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_AIRPOD);
        final Long cartItem1Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product1Id, 1));
        final Long cartItem2Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product2Id, 2));

        final ExtractableResponse<Response> response = 카트_아이템_주문_요청(
                savedMember,
                new PayRequest(List.of(cartItem1Id, cartItem2Id), 1_543_490, 1000)
        );

        final List<Long> orderIds = orderRepository.findOrderIdsOfMember(savedMember);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(orderIds.contains(response.jsonPath().getLong("orderId"))).isTrue()
        );
    }

    @Test
    void 카트에_담긴_아이템의_총_가격이_변동하면_예외가_발생한다() {
        final Member savedMember = memberRepository.addMember(MEMBER_GITCHAN, JOIN_EVENT_POINT);
        final Long product1Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);
        final Long product2Id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_AIRPOD);
        final Long cartItem1Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product1Id, 1));
        final Long cartItem2Id = 카트에_아이템_추가하고_아이디_반환(savedMember, new CartItemCreateRequest(product2Id, 2));

        final ExtractableResponse<Response> response = 카트_아이템_주문_요청(
                savedMember,
                new PayRequest(List.of(cartItem1Id, cartItem2Id), 1_543_491, 1000)
        );

        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }
}


