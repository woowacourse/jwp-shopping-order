package cart.acceptance;

import static cart.acceptance.steps.CommonSteps.비정상_요청;
import static cart.acceptance.steps.CommonSteps.요청_결과가_반환하는_리소스의_위치가_존재하는지_확인한다;
import static cart.acceptance.steps.CommonSteps.요청_결과의_상태를_검증한다;
import static cart.acceptance.steps.CommonSteps.정상_생성;
import static cart.acceptance.steps.CommonSteps.정상_요청;
import static cart.acceptance.steps.CommonSteps.정상_요청이지만_반환값_없음;
import static cart.acceptance.steps.ProductSteps.상품_번호를_구한다;
import static cart.acceptance.steps.ProductSteps.상품_수정_요청;
import static cart.acceptance.steps.ProductSteps.상품_전체_조회_결과를_확인한다;
import static cart.acceptance.steps.ProductSteps.상품_전체_조회_요청;
import static cart.acceptance.steps.ProductSteps.상품_정보;
import static cart.acceptance.steps.ProductSteps.상품_제거_요청;
import static cart.acceptance.steps.ProductSteps.상품_조회_결과를_확인한다;
import static cart.acceptance.steps.ProductSteps.상품_조회_요청;
import static cart.acceptance.steps.ProductSteps.상품_추가_요청;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_8900원;

import cart.test.AcceptanceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Nested
    public class 상품을_추가할_때 {

        @Test
        void 상품이_정상적으로_추가된다() {
            // when
            final var 상품_추가_요청_결과 = 상품_추가_요청(상품_8900원);

            // then
            요청_결과의_상태를_검증한다(상품_추가_요청_결과, 정상_생성);
            요청_결과가_반환하는_리소스의_위치가_존재하는지_확인한다(상품_추가_요청_결과);
        }

        @Test
        void 이름이_100자_이상인_상품_등록을_요청하면_예외가_발생한다() {
            // given
            final var 상품명 = "상".repeat(101);

            // when
            final var 상품_추가_요청_결과 = 상품_추가_요청(상품명, "pizza.png", 10000L);

            // then
            요청_결과의_상태를_검증한다(상품_추가_요청_결과, 비정상_요청);
        }

        @Test
        void 가격이_음수인_상품_등록을_요청하면_예외가_발생한다() {
            // given
            final var 가격 = -1L;

            // when
            final var 상품_추가_요청_결과 = 상품_추가_요청("치즈피자", "pizza.png", 가격);

            // then
            요청_결과의_상태를_검증한다(상품_추가_요청_결과, 비정상_요청);
        }
    }

    @Nested
    public class 상품을_조회할_때 {

        @Test
        void 상품을_단일_조회한다() {
            // given
            final var 상품_추가_요청_결과 = 상품_추가_요청(상품_8900원);
            final var 상품_번호 = 상품_번호를_구한다(상품_추가_요청_결과);

            // when
            final var 상품_조회_결과 = 상품_조회_요청(상품_번호);

            // then
            상품_조회_결과를_확인한다(상품_조회_결과, 상품_정보(상품_번호, 상품_8900원));
        }

        @Test
        void 상품을_전체_조회한다() {
            // given
            final var 상품1_추가_요청_결과 = 상품_추가_요청(상품_8900원);
            final var 상품1_번호 = 상품_번호를_구한다(상품1_추가_요청_결과);
            final var 상품2_추가_요청_결과 = 상품_추가_요청(상품_18900원);
            final var 상품2_번호 = 상품_번호를_구한다(상품2_추가_요청_결과);

            // when
            final var 상품_전체_조회_결과 = 상품_전체_조회_요청();

            // then
            상품_전체_조회_결과를_확인한다(
                    상품_전체_조회_결과,
                    상품_정보(상품1_번호, 상품_8900원),
                    상품_정보(상품2_번호, 상품_18900원)
            );
        }
    }

    @Test
    void 상품을_수정한다() {
        // given
        final var 상품_추가_요청_결과 = 상품_추가_요청(상품_8900원);
        final var 상품_번호 = 상품_번호를_구한다(상품_추가_요청_결과);

        // when
        final var 상품_수정_결과 = 상품_수정_요청(상품_번호, "치킨", "치킨.png", 30000L);

        // then
        요청_결과의_상태를_검증한다(상품_수정_결과, 정상_요청);
        final var 상품_조회_결과 = 상품_조회_요청(상품_번호);
        상품_조회_결과를_확인한다(상품_조회_결과, 상품_정보(상품_번호, "치킨", "치킨.png", 30000L));
    }

    @Test
    void 상품을_제거한다() {
        // given
        final var 상품_추가_요청_결과 = 상품_추가_요청(상품_8900원);
        final var 상품_번호 = 상품_번호를_구한다(상품_추가_요청_결과);

        // when
        final var 상품_제거_결과 = 상품_제거_요청(상품_번호);

        // then
        요청_결과의_상태를_검증한다(상품_제거_결과, 정상_요청이지만_반환값_없음);
    }
}
