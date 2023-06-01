package cart.integration;

import static cart.integration.IntegrationTestFixture.응답_코드_검증;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private Member 밀리;
    private Member 박스터;
    private Member 잘못된_사용자;

    private Product 상품_치킨;
    private Product 상품_피자;

    private CartItem 장바구니_밀리_치킨;
    private CartItem 장바구니_밀리_피자;

    @BeforeEach
    void init() {
        밀리 = memberRepository.save(new Member("millie@email.com", "millie"));
        박스터 = memberRepository.save(new Member("boxster@email.com", "boxster"));
        상품_치킨 = productRepository.save(new Product("치킨", 10000, "http://chicken.com"));
        상품_피자 = productRepository.save(new Product("피자", 20000, "http://pizza.com"));
        잘못된_사용자 = new Member(밀리.getId(), 밀리.getEmail(), 밀리.getPassword() + "asdf");
        장바구니_밀리_치킨 = cartItemRepository.save(new CartItem(상품_치킨, 밀리));
        장바구니_밀리_피자 = cartItemRepository.save(new CartItem(상품_피자, 밀리));
    }

    private ExtractableResponse<Response> 주문_요청(Member 사용자, int 배송비, int 총_가격, Long... 장바구니_ID) {
        List<Long> ids = Arrays.asList(장바구니_ID);
        OrderRequest request = new OrderRequest(ids, 배송비, BigDecimal.valueOf(총_가격));
        return RestAssured.given()
                .auth().preemptive().basic(사용자.getEmail(), 사용자.getPassword())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/orders")
                .then()
                .extract();

    }

    @Nested
    class 주문을_할_때 {

        @Test
        void 정상_주문한다() {
            var 응답 = 주문_요청(밀리, 3000, 33000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            응답_코드_검증(응답, HttpStatus.CREATED);
        }

        @Test
        void 다른_사용자_정보로_요청시_실패한다() {
            var 응답 = 주문_요청(박스터, 3000, 33000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            응답_코드_검증(응답, HttpStatus.FORBIDDEN);
        }

        @Test
        void 요청한_상품의_총_금액이_계산한_금액과_다르면_실패한다() {
            var 응답 = 주문_요청(밀리, 3000, 33001, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            응답_코드_검증(응답, HttpStatus.BAD_REQUEST);
        }
    }
}
