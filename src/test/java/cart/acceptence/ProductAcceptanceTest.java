package cart.acceptence;

import cart.dto.response.ProductResponse;
import cart.dto.response.exception.CartItemIdExceptionResponse;
import cart.dto.response.exception.ExceptionResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static cart.acceptence.fixtures.ProductFixtures.존재하지_않는_상품_아이디;
import static cart.acceptence.fixtures.ProductFixtures.치킨_10000원;
import static cart.acceptence.fixtures.ProductFixtures.피자_15000원;
import static cart.acceptence.fixtures.ProductFixtures.피자_18000원;
import static cart.acceptence.steps.ProductSteps.단일_상품_조회_요청;
import static cart.acceptence.steps.ProductSteps.상품_삭제_요청;
import static cart.acceptence.steps.ProductSteps.상품_수정_요청;
import static cart.acceptence.steps.ProductSteps.상품_추가_요청;
import static cart.acceptence.steps.ProductSteps.상품_추가하고_아이디_반환;
import static cart.acceptence.steps.ProductSteps.전체_상품_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("상품 관리 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Nested
    class 상품을_추가할_때 {

        @Test
        void 정상_요청이면_성공적으로_추가한다() {
            // when
            ExtractableResponse<Response> 상품_생성_결과 = 상품_추가_요청(피자_15000원);

            // then
            assertThat(상품_생성_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(상품_생성_결과.header("Location")).isNotBlank();
        }

        @Test
        void 중복된_상품은_추가할_수_없다() {
            //given
            상품_추가_요청(피자_15000원);

            // when
            ExtractableResponse<Response> 상품_추가_결과 = 상품_추가_요청(피자_15000원);

            // then
            assertThat(상품_추가_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(상품_추가_결과.jsonPath().getObject("payload", ExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new ExceptionResponse("이미 존재하는 상품입니다"));
        }

    }

    @Test
    void 추가한_상품을_조회한다() {
        // given
        long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);

        // when
        ExtractableResponse<Response> 상품_조회_결과 = 단일_상품_조회_요청(피자_아이디);
        ProductResponse 조회된_상품 = 상품_조회_결과.jsonPath().getObject(".", ProductResponse.class);

        // then
        assertThat(상품_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(조회된_상품.getId()).isEqualTo(피자_아이디);
        assertThat(조회된_상품.getName()).isEqualTo(피자_15000원.getName());
        assertThat(조회된_상품.getPrice()).isEqualTo(피자_15000원.getPrice());
    }

    @Test
    void 전체_상품_목록을_조회한다() {
        // given
        long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);
        long 치킨_아이디 = 상품_추가하고_아이디_반환(치킨_10000원);

        // when
        ExtractableResponse<Response> 전체_상품_조회_결과 = 전체_상품_조회_요청();
        List<Long> 상품_아이디_목록 = 전체_상품_조회_결과.jsonPath().getList(".", ProductResponse.class).stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        // then
        assertThat(전체_상품_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(상품_아이디_목록).containsAll(List.of(피자_아이디, 치킨_아이디));
    }

    @Nested
    class 상품을_수정할_때 {

        @Test
        void 정상_요청이면_성공적으로_수정한다() {
            //given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);

            //when
            ExtractableResponse<Response> 상품_수정_결과 = 상품_수정_요청(피자_아이디, 피자_18000원);

            //then
            assertThat(상품_수정_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        void 다른_상품과_중복되게_수정할_수_없다() {
            //given
            상품_추가_요청(치킨_10000원);
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);

            // when
            ExtractableResponse<Response> 상품_수정_결과 = 상품_수정_요청(피자_아이디, 치킨_10000원);

            // then
            assertThat(상품_수정_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(상품_수정_결과.jsonPath().getObject("payload", ExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new ExceptionResponse("이미 존재하는 상품입니다"));
        }
    }

    @Nested
    class 상품을_삭제할_때 {

        @Test
        void 정상_요청이면_성공적으로_삭제한다() {
            //given
            long 피자_아이디 = 상품_추가하고_아이디_반환(피자_15000원);

            //when
            ExtractableResponse<Response> 상품_삭제_결과 = 상품_삭제_요청(피자_아이디);

            //then
            assertThat(상품_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        }

        @Test
        void 존재하지_않는_상품_아이디면_삭제할_수_없다() {
            //when
            ExtractableResponse<Response> 상품_삭제_결과 = 상품_삭제_요청(존재하지_않는_상품_아이디);

            //then
            assertThat(상품_삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(상품_삭제_결과.jsonPath().getObject("payload", ExceptionResponse.class))
                    .usingRecursiveComparison()
                    .isEqualTo(new ExceptionResponse("존재하지 않는 상품입니다"));
        }
    }

}
