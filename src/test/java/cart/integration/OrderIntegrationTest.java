package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.dto.ProductRequest;
import cart.integration.step.CartItemStep;
import cart.integration.step.OrderStep;
import cart.integration.step.ProductStep;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    private Member 사용자;
    private OrderRequest 주문;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        사용자 = memberRepository.findById(1L);

        List<CartItem> cartItems = cartItemRepository.findByMemberId(사용자.getId());
        List<Long> cartItemIds = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        주문 = new OrderRequest(
                cartItemIds,
                "1234-1234-1234-1234",
                123,
                300
        );
    }

    @DisplayName("장바구니의 아이템을 주문한다.")
    @Test
    void saveOrder() {
        // when
        ExtractableResponse<Response> 주문_추가_응답 = OrderStep.주문을_추가한다(사용자, 주문);
        long 주문_ID = OrderStep.응답에서_아이디를_가져온다(주문_추가_응답);

        ExtractableResponse<Response> 주문_상세_조회_응답 = OrderStep.주문_상세_조회한다(사용자, 주문_ID);
        OrderDetailResponse orderDetailResponse = 주문_상세_조회_응답.jsonPath()
                .getObject(".", OrderDetailResponse.class);

        // then
        assertAll(
                () -> assertThat(주문_추가_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderDetailResponse.getUsedPoint()).isEqualTo(300),
                () -> assertThat(orderDetailResponse.getSavedPoint()).isEqualTo(1000),
                () -> assertThat(orderDetailResponse.getProducts()).hasSize(2)
        );
    }

    @DisplayName("잘못된 사용자 정보로 주문 추가 요청시 실패한다.")
    @Test
    void saveOrderByIllegalMember() {
        // given
        Member 잘못된_사용자 = new Member(사용자.getId(), 사용자.getEmail(), 사용자.getPassword() + "asdf", 1000);

        // when
        ExtractableResponse<Response> 잘못된_주문_추가_응답 = OrderStep.주문을_추가한다(잘못된_사용자, 주문);

        // then
        assertThat(잘못된_주문_추가_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("주문의 상세 내역을 조회한다")
    @Test
    void findOrderDetail() {
        // given
        ExtractableResponse<Response> response = OrderStep.주문을_추가한다(사용자, 주문);
        long 주문_ID = OrderStep.응답에서_아이디를_가져온다(response);

        // when
        ExtractableResponse<Response> 주문_상세_조회_응답 = OrderStep.주문_상세_조회한다(사용자, 주문_ID);
        OrderDetailResponse orderDetailResponse = 주문_상세_조회_응답.jsonPath()
                .getObject(".", OrderDetailResponse.class);

        // then
        assertAll(
                () -> assertThat(주문_상세_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderDetailResponse.getUsedPoint()).isEqualTo(300),
                () -> assertThat(orderDetailResponse.getSavedPoint()).isEqualTo(1000),
                () -> assertThat(orderDetailResponse.getProducts()).hasSize(2)
        );
    }

    @DisplayName("잘못된 사용자 정보로 주문 조회 요청시 실패한다.")
    @Test
    void findOrderDetailByIllegalMember() {
        // given
        ExtractableResponse<Response> response = OrderStep.주문을_추가한다(사용자, 주문);
        long 주문_ID = OrderStep.응답에서_아이디를_가져온다(response);
        Member 잘못된_사용자 = new Member(사용자.getId(), 사용자.getEmail(), 사용자.getPassword() + "asdf", 1000);

        // when
        ExtractableResponse<Response> 잘못된_주문_상세_조회_응답 = OrderStep.주문_상세_조회한다(잘못된_사용자, 주문_ID);

        // then
        assertThat(잘못된_주문_상세_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자별 주문 목록을 조회한다.")
    @Test
    void findOrderByMember() {
        // given
        OrderStep.주문을_추가한다(사용자, 주문);

        Long 치킨_ID = ProductStep.상품_추가_응답에서_아이디를_가져온다(
                new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long new_장바구니_ID = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 치킨_ID);
        OrderRequest 주문2 = new OrderRequest(
                List.of(new_장바구니_ID),
                "1234-1234-1234-1234",
                123,
                300
        );

        OrderStep.주문을_추가한다(사용자, 주문2);

        // when
        ExtractableResponse<Response> 사용자별_주문_응답 = OrderStep.사용자별_주문을_조회한다(사용자);
        List<OrderDetailResponse> orderDetailResponses = 사용자별_주문_응답.jsonPath()
                .getList(".", OrderDetailResponse.class);

        // then
        assertAll(
                () -> assertThat(사용자별_주문_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderDetailResponses).hasSize(2)
        );
    }
}
