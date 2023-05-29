package cart.acceptance.product;

import static cart.acceptance.common.CommonAcceptanceSteps.생성된_ID;
import static cart.acceptance.common.CommonAcceptanceSteps.응답을_검증한다;
import static cart.acceptance.common.CommonAcceptanceSteps.정상_생성;
import static cart.acceptance.product.ProductSteps.단일_상품_조회_요청;
import static cart.acceptance.product.ProductSteps.모든_상품_조회_요청;
import static cart.acceptance.product.ProductSteps.상품_생성_요청;
import static cart.acceptance.product.ProductSteps.상품_전체_조회_결과를_검증한다;
import static cart.acceptance.product.ProductSteps.상품_조회_결과를_검증한다;
import static cart.acceptance.product.ProductSteps.예상_상품_정보;

import cart.acceptance.AcceptanceTest;
import cart.product.presentation.dto.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Product 통합 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    public void 상품을_생성한다() {
        var 응답 = 상품_생성_요청(
                "치킨",
                10_000,
                "http://example.com/chicken.jpg"
        );
        응답을_검증한다(응답, 정상_생성);
    }

    @Test
    public void 단일_상품을_조회한다() {
        var 응답 = 상품_생성_요청(
                "피자",
                15_000,
                "http://example.com/pizza.jpg"
        );
        Long 상품_ID = 생성된_ID(응답);

        // when
        var 상품_정보 = 단일_상품_조회_요청(상품_ID);

        // then
        var 피자 = 예상_상품_정보(상품_ID, "피자", 15_000, "http://example.com/pizza.jpg");
        상품_조회_결과를_검증한다(상품_정보, 피자);
    }

    @Test
    public void 모든_상품을_조회한다() {
        // given
        상품_생성_요청("피자", 15000, "http://ex.com/pizza.jpg");
        상품_생성_요청("말랑", 999999, "http://ex.com/mallang.jpg");
        상품_생성_요청("코코닭", 10, "http://ex.com/kokochiken.jpg");

        // when
        var 모든_상품_정보들 = 모든_상품_조회_요청();

        List<ProductResponse> 예상_상품_정보들 = List.of(
                예상_상품_정보(null, "피자", 15000, "http://ex.com/pizza.jpg"),
                예상_상품_정보(null, "말랑", 999999, "http://ex.com/mallang.jpg"),
                예상_상품_정보(null, "코코닭", 10, "http://ex.com/kokochiken.jpg")
        );
        상품_전체_조회_결과를_검증한다(모든_상품_정보들, 예상_상품_정보들);
    }
}
