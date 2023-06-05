package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductRequest;
import cart.integration.step.CartItemStep;
import cart.integration.step.ProductStep;
import cart.repository.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;

    private Long 치킨_ID;
    private Long 피자_ID;
    private Member 사용자;
    private Member 사용자2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        치킨_ID = ProductStep.상품_추가_응답에서_아이디를_가져온다(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        피자_ID = ProductStep.상품_추가_응답에서_아이디를_가져온다(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        사용자 = memberRepository.findById(1L);
        사용자2 = memberRepository.findById(2L);
    }

    @DisplayName("장바구니에 아이템을 추가한다.")
    @Test
    void addCartItem() {
        // given
        CartItemRequest 치킨_추가_요청 = new CartItemRequest(치킨_ID);

        // when
        ExtractableResponse<Response> 장바구니_아이템_추가_응답 = CartItemStep.장바구니에_아이템을_추가한다(사용자, 치킨_추가_요청);

        // then
        assertThat(장바구니_아이템_추가_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 사용자 정보로 장바구니에 아이템을 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        // given
        Member 잘못된_사용자 = new Member(사용자.getId(), 사용자.getEmail(), 사용자.getPassword() + "asdf", 1000);
        CartItemRequest 치킨_추가_요청 = new CartItemRequest(치킨_ID);

        // when
        ExtractableResponse<Response> 잘못된_장바구니_추가_응답 = CartItemStep.장바구니에_아이템을_추가한다(잘못된_사용자, 치킨_추가_요청);

        // then
        assertThat(잘못된_장바구니_추가_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자가 담은 장바구니 아이템을 조회한다.")
    @Test
    void getCartItems() {
        // given
        Long 장바구니_추가된_치킨_ID = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 치킨_ID);
        Long 장바구니_추가된_피자_ID = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 피자_ID);

        // when
        ExtractableResponse<Response> 사용자_장바구니_조회_응답 = CartItemStep.장바구니의_아이템을_조회한다(사용자);
        List<Long> resultCartItemIds = 사용자_장바구니_조회_응답.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());

        // then
        Assertions.assertAll(
                () -> assertThat(사용자_장바구니_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(resultCartItemIds).containsAll(Arrays.asList(장바구니_추가된_치킨_ID, 장바구니_추가된_피자_ID))
        );
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 변경한다.")
    @Test
    void increaseCartItemQuantity() {
        // given
        Long cartItemId = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 치킨_ID);

        // when
        ExtractableResponse<Response> 장바구니_수량_업데이트_응답 = CartItemStep.장바구니_아이템의_수량을_업데이트한다(사용자, cartItemId, 10);
        ExtractableResponse<Response> 장바구니_조회_응답 = CartItemStep.장바구니의_아이템을_조회한다(사용자);
        Optional<CartItemResponse> selectedCartItemResponse = 장바구니_조회_응답.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(cartItemId))
                .findFirst();

        // then
        Assertions.assertAll(
                () -> assertThat(장바구니_수량_업데이트_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(selectedCartItemResponse.isPresent()).isTrue(),
                () -> assertThat(selectedCartItemResponse.get().getQuantity()).isEqualTo(10)
        );
    }

    @DisplayName("장바구니에 담긴 아이템의 수량을 0으로 변경하면, 장바구니에서 아이템이 삭제된다.")
    @Test
    void decreaseCartItemQuantityToZero() {
        // given
        Long 장바구니_추가된_치킨_ID = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 치킨_ID);

        // when
        ExtractableResponse<Response> 장바구니_수량_업데이트_응답 = CartItemStep.장바구니_아이템의_수량을_업데이트한다(사용자, 장바구니_추가된_치킨_ID, 0);
        ExtractableResponse<Response> 장바구니_조회_응답 = CartItemStep.장바구니의_아이템을_조회한다(사용자);
        Optional<CartItemResponse> selectedCartItemResponse = 장바구니_조회_응답.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(장바구니_추가된_치킨_ID))
                .findFirst();

        // then
        Assertions.assertAll(
                () -> assertThat(장바구니_수량_업데이트_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(selectedCartItemResponse.isPresent()).isFalse()
        );
    }

    @DisplayName("다른 사용자가 담은 장바구니 아이템의 수량을 변경하려 하면 실패한다.")
    @Test
    void updateOtherMembersCartItem() {
        // given
        Long 장바구니_추가된_치킨_ID = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 치킨_ID);

        // when
        ExtractableResponse<Response> 잘못된_장바구니_업데이트_응답 = CartItemStep.장바구니_아이템의_수량을_업데이트한다(사용자2, 장바구니_추가된_치킨_ID, 10);

        // then
        assertThat(잘못된_장바구니_업데이트_응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("장바구니에 담긴 아이템을 삭제한다.")
    @Test
    void removeCartItem() {
        // given
        Long 장바구니_추가된_치킨_ID = CartItemStep.장바구니에_아이뎀_추가_응답에서_아이디를_가져온다(사용자, 치킨_ID);

        // when
        ExtractableResponse<Response> 장바구니_아이템_삭제_응답 = CartItemStep.장바구니의_아이템을_삭제한다(사용자, 장바구니_추가된_치킨_ID);
        ExtractableResponse<Response> 장바구니_조회_응답 = CartItemStep.장바구니의_아이템을_조회한다(사용자);
        Optional<CartItemResponse> selectedCartItemResponse = 장바구니_조회_응답.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .filter(cartItemResponse -> cartItemResponse.getId().equals(장바구니_추가된_치킨_ID))
                .findFirst();

        // then
        Assertions.assertAll(
                () -> assertThat(장바구니_아이템_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(selectedCartItemResponse.isPresent()).isFalse()
        );
    }
}
