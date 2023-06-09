package cart.integration;

import static cart.integration.IntegrationTestFixture.아이디를_반환한다;
import static cart.integration.IntegrationTestFixture.응답_코드_검증;
import static cart.integration.ProductIntegrationTestFixture.상품_등록_요청;
import static cart.integration.ProductIntegrationTestFixture.상품_삭제_요청;
import static cart.integration.ProductIntegrationTestFixture.상품_수정_요청;
import static cart.integration.ProductIntegrationTestFixture.상품_응답;
import static cart.integration.ProductIntegrationTestFixture.상품_전체_조회_요청;
import static cart.integration.ProductIntegrationTestFixture.상품_전체_조회_응답_검증;
import static cart.integration.ProductIntegrationTestFixture.상품_조회_요청;
import static cart.integration.ProductIntegrationTestFixture.조회_응답_검증;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class ProductIntegrationTest extends IntegrationTest {


    @Nested
    class 상품을_등록할_때 {


        @Test
        void 정상_등록한다() {
            var 응답 = 상품_등록_요청("치킨", 10000, "http://chicken.com");

            응답_코드_검증(응답, HttpStatus.CREATED);
        }

    }

    @Nested
    class 상품을_조회할_때 {


        @Test
        void 정상_조회한다() {
            Long 상품_ID = 아이디를_반환한다(상품_등록_요청("치킨", 10000, "http://chicken.com"));

            var 응답 = 상품_조회_요청(상품_ID);

            응답_코드_검증(응답, HttpStatus.OK);
            조회_응답_검증(응답, 상품_응답(상품_ID, "치킨", 10000, "http://chicken.com"));
        }

        @Test
        void 존재하지_않는_상품_조회_요청시_실패한다() {
            var 응답 = 상품_조회_요청(Long.MAX_VALUE);

            응답_코드_검증(응답, HttpStatus.NOT_FOUND);
        }

    }

    @Nested
    class 상품_전체_조회할_때 {


        @Test
        void 정상_전체_조회한다() {
            Long 상품_치킨_ID = 아이디를_반환한다(상품_등록_요청("치킨", 10000, "http://chicken.com"));
            Long 상품_피자_ID = 아이디를_반환한다(상품_등록_요청("피자", 20000, "http://pizza.com"));

            var 응답 = 상품_전체_조회_요청();

            응답_코드_검증(응답, HttpStatus.OK);
            상품_전체_조회_응답_검증(
                    응답,
                    상품_응답(상품_치킨_ID, "치킨", 10000, "http://chicken.com"),
                    상품_응답(상품_피자_ID, "피자", 20000, "http://pizza.com")
            );
        }

    }

    @Nested
    class 상품을_수정할_때 {


        @Test
        void 정상_수정한다() {
            Long 상품_치킨_ID = 아이디를_반환한다(상품_등록_요청("치킨", 10000, "http://chicken.com"));

            var 응답 = 상품_수정_요청(상품_치킨_ID, "양념치킨", 20000, "http://spicy-chicken.com");

            응답_코드_검증(응답, HttpStatus.OK);
            조회_응답_검증(상품_조회_요청(상품_치킨_ID), 상품_응답(상품_치킨_ID, "양념치킨", 20000, "http://spicy-chicken.com"));
        }

        @Test
        void 존재하지_않는_상품_수정_요청시_실패한다() {
            var 응답 = 상품_수정_요청(Long.MAX_VALUE, "양념치킨", 20000, "http://spicy-chicken.com");

            응답_코드_검증(응답, HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class 상품을_삭제할_때 {

        @Test
        void 정상_삭제한다() {
            Long 상품_치킨_ID = 아이디를_반환한다(상품_등록_요청("치킨", 10000, "http://chicken.com"));

            var 응답 = 상품_삭제_요청(상품_치킨_ID);

            응답_코드_검증(응답, HttpStatus.NO_CONTENT);
            상품_전체_조회_응답_검증(상품_전체_조회_요청());
        }
    }
}
