package cart.acceptence;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.acceptence.fixtures.ProductFixtures.치킨_10000원;
import static cart.acceptence.fixtures.ProductFixtures.피자_15000원;
import static cart.acceptence.steps.CartItemSteps.장바구니_아이템_삭제_요청;
import static cart.acceptence.steps.CartItemSteps.장바구니_아이템_수정_요청;
import static cart.acceptence.steps.CartItemSteps.장바구니_아이템_추가_요청;
import static cart.acceptence.steps.CartItemSteps.장바구니_아이템_추가하고_아이디_반환;
import static cart.acceptence.steps.CartItemSteps.장바구니_조회_요청;
import static cart.acceptence.steps.CartItemSteps.장바구니_조회하고_특정_아이템이_존재하면_반환;
import static cart.acceptence.steps.ProductSteps.상품_추가하고_아이디_반환;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("장바구니 관리 기능")
public class CartItemAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberDao memberDao;
    private Member 등록된_사용자1;
    private Member 등록된_사용자2;

    @BeforeEach
    void setUp() {
        super.setUp();

        등록된_사용자1 = memberDao.getMemberById(1L);
        등록된_사용자2 = memberDao.getMemberById(2L);
    }

    @Nested
    class 장바구니에_아이템을_추가할_때 {

        @Test
        void 정상_요청이면_성공적으로_추가한다() {
            //given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);

            // when
            ExtractableResponse<Response> 장바구니_아이템_추가_결과 = 장바구니_아이템_추가_요청(등록된_사용자1, 피자_아이디);

            // then
            assertThat(장바구니_아이템_추가_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(장바구니_아이템_추가_결과.header("Location")).isNotBlank();
        }

/*        @Test
        void 장바구니에_이미_담긴_상품은_추가할_수_없다() {
            //given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            장바구니_아이템_추가_요청(등록된_사용자1, 피자_아이디);

            // when
            ExtractableResponse<Response> 장바구니_아이템_추가_결과 = 장바구니_아이템_추가_요청(등록된_사용자1, 피자_아이디);

            // then
            assertThat(장바구니_아이템_추가_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(장바구니_아이템_추가_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 이미 장바구니에 담긴 상품입니다.");
        }*/

        @Test
        void 사용자_정보가_잘못되었다면_추가할_수_없다() {
            //given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            Member 잘못된_사용자 = new Member(등록된_사용자1.getId(), 등록된_사용자1.getEmail(), 등록된_사용자1.getPassword() + "illegal");

            // when
            ExtractableResponse<Response> 장바구니_아이템_추가_결과 = 장바구니_아이템_추가_요청(잘못된_사용자, 피자_아이디);

            assertThat(장바구니_아이템_추가_결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
/*            assertThat(장바구니_아이템_추가_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 존재하지 않는 사용자입니다.");*/
        }

    }

    @Test
    void 장바구니를_조회한다() {
        // given
        long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
        long 치킨_아이디 = 상품_추가하고_아이디_반환(치킨_10000원);
        long 장바구니_피자_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 피자_아이디);
        long 장바구니_치킨_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 치킨_아이디);

        // when
        ExtractableResponse<Response> 장바구니_조회_결과 = 장바구니_조회_요청(등록된_사용자1);
        List<Long> 장바구니에_있는_아이템_아이디 = 장바구니_조회_결과.jsonPath()
                .getList(".", CartItemResponse.class)
                .stream()
                .map(CartItemResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(장바구니_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(장바구니에_있는_아이템_아이디).containsAll(Arrays.asList(장바구니_피자_아이디, 장바구니_치킨_아이디));
    }

    @Nested
    class 장바구니_아이템의_수량을_수정할_때 {

        @Test
        void 수량이_0이아니게_수정되면_장바구니에_아이템이_유지된다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 장바구니_피자_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 피자_아이디);

            // when
            ExtractableResponse<Response> 장바구니_아이템_수정_결과 = 장바구니_아이템_수정_요청(
                    등록된_사용자1,
                    장바구니_피자_아이디,
                    10
            );
            Optional<CartItemResponse> 장바구니_피자_아이템 = 장바구니_조회하고_특정_아이템이_존재하면_반환(등록된_사용자1, 장바구니_피자_아이디);

            // then
            assertThat(장바구니_아이템_수정_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(장바구니_피자_아이템.isPresent()).isTrue();
            assertThat(장바구니_피자_아이템.get().getQuantity()).isEqualTo(10);
        }

        @Test
        void 수량이_0이되면_장바구니에서_아이템이_삭제된다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 장바구니_피자_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 피자_아이디);

            // when
            ExtractableResponse<Response> 장바구니_아이템_수정_결과 = 장바구니_아이템_수정_요청(
                    등록된_사용자1,
                    장바구니_피자_아이디,
                    0
            );
            Optional<CartItemResponse> 장바구니_피자_아이템 = 장바구니_조회하고_특정_아이템이_존재하면_반환(등록된_사용자1, 장바구니_피자_아이디);

            // then
            assertThat(장바구니_아이템_수정_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(장바구니_피자_아이템.isPresent()).isFalse();
        }

        @Test
        void 타인의_장바구니를_수정할_수_없다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 장바구니_피자_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 피자_아이디);

            // when
            ExtractableResponse<Response> 장바구니_아이템_수정_결과 = 장바구니_아이템_수정_요청(
                    등록된_사용자2,
                    장바구니_피자_아이디,
                    10
            );

            // then
            assertThat(장바구니_아이템_수정_결과.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
/*            assertThat(장바구니_아이템_수정_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 잘못된 접근입니다.");*/
        }

    }

    @Nested
    class 장바구니_아이템을_삭제할_때 {

        @Test
        void 정상_요청이면_성공적으로_삭제한다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 장바구니_피자_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 피자_아이디);

            // when
            ExtractableResponse<Response> 장바구니_아이템_삭제_결과 = 장바구니_아이템_삭제_요청(등록된_사용자1, 장바구니_피자_아이디);
            Optional<CartItemResponse> 장바구니_피자_아이템 = 장바구니_조회하고_특정_아이템이_존재하면_반환(등록된_사용자1, 장바구니_피자_아이디);

            // then
            assertThat(장바구니_아이템_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(장바구니_피자_아이템.isPresent()).isFalse();
        }

        @Test
        void 타인의_장바구니를_삭제할_수_없다() {
            // given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
            long 장바구니_피자_아이디 = 장바구니_아이템_추가하고_아이디_반환(등록된_사용자1, 피자_아이디);

            // when
            ExtractableResponse<Response> 장바구니_아이템_삭제_결과 = 장바구니_아이템_삭제_요청(등록된_사용자2, 장바구니_피자_아이디);

            // then
            assertThat(장바구니_아이템_삭제_결과.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
/*            assertThat(장바구니_아이템_삭제_결과.jsonPath().getObject("message", String.class))
                    .isEqualTo("[ERROR] 잘못된 접근입니다.");*/
        }
    }
}
