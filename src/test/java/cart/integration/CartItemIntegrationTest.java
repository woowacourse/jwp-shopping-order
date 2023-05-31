package cart.integration;

import static cart.integration.CartItemIntegrationTestFixture.장바구니_삭제_요청;
import static cart.integration.CartItemIntegrationTestFixture.장바구니_상품_수량_수정_요청;
import static cart.integration.CartItemIntegrationTestFixture.장바구니_상품_전체_조회_요청;
import static cart.integration.CartItemIntegrationTestFixture.장바구니_응답;
import static cart.integration.CartItemIntegrationTestFixture.장바구니_전체_조회_응답_검증;
import static cart.integration.CartItemIntegrationTestFixture.장바구니에_상품_등록_요청;
import static cart.integration.IntegrationTestFixture.아이디를_반환한다;
import static cart.integration.IntegrationTestFixture.응답_코드_검증;

import cart.domain.Member;
import cart.domain.Product;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member 밀리;
    private Member 박스터;
    private Member 잘못된_사용자;

    private Product 상품_치킨;
    private Product 상품_피자;

    @BeforeEach
    void init() {
        밀리 = memberRepository.save(new Member("millie@email.com", "millie"));
        박스터 = memberRepository.save(new Member("boxster@email.com", "boxster"));
        상품_치킨 = productRepository.save(new Product("치킨", 10000, "http://chicken.com"));
        상품_피자 = productRepository.save(new Product("피자", 20000, "http://pizza.com"));
        잘못된_사용자 = new Member(밀리.getId(), 밀리.getEmail(), 밀리.getPassword() + "asdf");
    }

    @Nested
    class 장바구니에_상품을_등록할_때 {

        @Test
        void 정상_추가한다() {
            var 응답 = 장바구니에_상품_등록_요청(밀리, 상품_치킨.getId());

            응답_코드_검증(응답, HttpStatus.CREATED);
        }

        @Test
        void 잘못된_사용자_정보로_요청시_실패한다() {
            var 응답 = 장바구니에_상품_등록_요청(잘못된_사용자, 상품_치킨.getId());

            응답_코드_검증(응답, HttpStatus.UNAUTHORIZED);
        }

    }

    @Nested
    class 장바구니에서_상품을_전체_조회할_때 {

        @Test
        void 정상_전체_조회한다() {
            Long firstSaveID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_치킨.getId()));
            Long secondSaveID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_피자.getId()));

            var 응답 = 장바구니_상품_전체_조회_요청(밀리);

            응답_코드_검증(응답, HttpStatus.OK);
            장바구니_전체_조회_응답_검증(응답, 장바구니_응답(firstSaveID, 상품_치킨, 1), 장바구니_응답(secondSaveID, 상품_피자, 1));
        }

    }

    @Nested
    class 장바구니의_상품_수량을_수정할_때 {

        @Test
        void 정상_수정한다() {
            Long 장바구니_ID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_치킨.getId()));

            var 응답 = 장바구니_상품_수량_수정_요청(밀리, 장바구니_ID, 10);

            응답_코드_검증(응답, HttpStatus.OK);
            장바구니_전체_조회_응답_검증(장바구니_상품_전체_조회_요청(밀리), 장바구니_응답(장바구니_ID, 상품_치킨, 10));
        }

        @Test
        void 다른_사용자의_장바구니의_수량을_수정_요청시_실패한다() {
            Long 장바구니_ID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_치킨.getId()));

            var 응답 = 장바구니_상품_수량_수정_요청(박스터, 장바구니_ID, 10);

            응답_코드_검증(응답, HttpStatus.FORBIDDEN);
        }

        @Test
        void 장바구니의_수량을_0으로_수정_요청시_장바구니를_삭제한다() {
            Long 장바구니_ID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_치킨.getId()));

            var 응답 = 장바구니_상품_수량_수정_요청(밀리, 장바구니_ID, 0);

            응답_코드_검증(응답, HttpStatus.OK);
            장바구니_전체_조회_응답_검증(장바구니_상품_전체_조회_요청(밀리));
        }
    }

    @Nested
    class 장바구니를_삭제_요청할_때 {

        @Test
        void 정상_삭제한다() {
            Long 장바구니_ID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_치킨.getId()));

            var 응답 = 장바구니_삭제_요청(밀리, 장바구니_ID);

            응답_코드_검증(응답, HttpStatus.NO_CONTENT);
            장바구니_전체_조회_응답_검증(장바구니_상품_전체_조회_요청(밀리));
        }

        @Test
        void 다른_사용자의_장바구니_삭제_요청시_실패한다() {
            Long 장바구니_ID = 아이디를_반환한다(장바구니에_상품_등록_요청(밀리, 상품_치킨.getId()));

            var 응답 = 장바구니_삭제_요청(박스터, 장바구니_ID);

            응답_코드_검증(응답, HttpStatus.FORBIDDEN);
        }
    }
}
