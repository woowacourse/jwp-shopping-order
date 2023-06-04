package cart.acceptance;

import static cart.acceptance.steps.CartItemSteps.단일_장바구니_URL;
import static cart.acceptance.steps.CartItemSteps.장바구니_URL;
import static cart.acceptance.steps.CartItemSteps.장바구니_상품_번호를_구한다;
import static cart.acceptance.steps.CartItemSteps.장바구니_수량_변경_요청_정보;
import static cart.acceptance.steps.CartItemSteps.장바구니_정보;
import static cart.acceptance.steps.CartItemSteps.장바구니_조회_결과를_확인한다;
import static cart.acceptance.steps.CartItemSteps.장바구니_추가_요청_정보;
import static cart.acceptance.steps.CommonSteps.수정;
import static cart.acceptance.steps.CommonSteps.요청_결과의_상태를_검증한다;
import static cart.acceptance.steps.CommonSteps.정상_생성;
import static cart.acceptance.steps.CommonSteps.정상_요청;
import static cart.acceptance.steps.CommonSteps.정상_요청이지만_반환값_없음;
import static cart.acceptance.steps.CommonSteps.제거;
import static cart.acceptance.steps.CommonSteps.조회;
import static cart.acceptance.steps.CommonSteps.추가;
import static cart.acceptance.steps.MemberSteps.사용자_저장;
import static cart.acceptance.steps.ProductSteps.상품_번호를_구한다;
import static cart.acceptance.steps.ProductSteps.상품_추가_요청;
import static cart.acceptance.steps.Request.Builder.사용자의_요청_생성;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_8900원;

import cart.repository.MemberRepository;
import cart.test.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemAcceptanceTest extends AcceptanceTest {

    private String 상품_8900원_상품_번호;
    private String 상품_18900원_상품_번호;

    @Autowired
    private MemberRepository 사용자_저장소;

    @BeforeEach
    void init() {
        사용자_저장(사용자_저장소, 사용자1);
        final var 상품_추가_요청_결과1 = 상품_추가_요청(상품_8900원);
        상품_8900원_상품_번호 = 상품_번호를_구한다(상품_추가_요청_결과1);
        final var 상품_추가_요청_결과2 = 상품_추가_요청(상품_18900원);
        상품_18900원_상품_번호 = 상품_번호를_구한다(상품_추가_요청_결과2);
    }

    @Test
    void 장바구니에_상품을_추가한다() {
        // when
        final var 요청_결과 = 사용자의_요청_생성(사용자1)
                .전송_정보(장바구니_추가_요청_정보(상품_8900원_상품_번호))
                .요청_위치(추가, 장바구니_URL)
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(요청_결과, 정상_생성);
        final var 전체_조회_결과 = 사용자의_요청_생성(사용자1).요청_위치(조회, 장바구니_URL).요청_결과_반환();
        장바구니_조회_결과를_확인한다(전체_조회_결과, 장바구니_정보(상품_8900원, 1));
    }

    @Test
    void 사용자의_장바구니에_담겨있는_모든_상품을_조회한다() {
        // given
        사용자의_요청_생성(사용자1).전송_정보(장바구니_추가_요청_정보(상품_8900원_상품_번호)).요청_위치(추가, 장바구니_URL);
        사용자의_요청_생성(사용자1).전송_정보(장바구니_추가_요청_정보(상품_18900원_상품_번호)).요청_위치(추가, 장바구니_URL);

        // when
        final var 전체_조회_결과 = 사용자의_요청_생성(사용자1)
                .요청_위치(조회, 장바구니_URL)
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(전체_조회_결과, 정상_요청);
        장바구니_조회_결과를_확인한다(전체_조회_결과, 장바구니_정보(상품_8900원, 1), 장바구니_정보(상품_18900원, 1));
    }

    @Test
    void 장바구니에_담겨있는_상품을_제거한다() {
        // given
        final var 장바구니_추가_요청_결과 = 사용자의_요청_생성(사용자1)
                .전송_정보(장바구니_추가_요청_정보(상품_8900원_상품_번호))
                .요청_위치(추가, 장바구니_URL)
                .요청_결과_반환();
        final var 장바구니_상품_번호 = 장바구니_상품_번호를_구한다(장바구니_추가_요청_결과);

        // when
        final var 제거_요청_결과 = 사용자의_요청_생성(사용자1)
                .요청_위치(제거, 단일_장바구니_URL(장바구니_상품_번호))
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(제거_요청_결과, 정상_요청이지만_반환값_없음);
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_변경한다() {
        // given
        final var 장바구니_추가_요청_결과 = 사용자의_요청_생성(사용자1)
                .전송_정보(장바구니_추가_요청_정보(상품_8900원_상품_번호))
                .요청_위치(추가, 장바구니_URL)
                .요청_결과_반환();
        final var 장바구니_상품_번호 = 장바구니_상품_번호를_구한다(장바구니_추가_요청_결과);

        // when
        final var 수정_요청_결과 = 사용자의_요청_생성(사용자1)
                .전송_정보(장바구니_수량_변경_요청_정보(2))
                .요청_위치(수정, 단일_장바구니_URL(장바구니_상품_번호))
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(수정_요청_결과, 정상_요청);
        final var 전체_조회_결과 = 사용자의_요청_생성(사용자1).요청_위치(조회, 장바구니_URL).요청_결과_반환();
        장바구니_조회_결과를_확인한다(전체_조회_결과, 장바구니_정보(상품_8900원, 2));
    }
}
