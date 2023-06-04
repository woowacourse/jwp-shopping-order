package cart.integration;

import static cart.integration.IntegrationTestFixture.아이디를_반환한다;
import static cart.integration.IntegrationTestFixture.응답_코드_검증;
import static cart.integration.MemberCouponIntegrationTestFixture.정률_쿠폰;
import static cart.integration.MemberCouponIntegrationTestFixture.쿠폰_전체_조회_응답;
import static cart.integration.MemberCouponIntegrationTestFixture.쿠폰_전체_조회_응답_검증;
import static cart.integration.MemberCouponIntegrationTestFixture.쿠폰_조회_요청;
import static cart.integration.OrderIntegrationTestFixture.사용자_주문_목록_응답;
import static cart.integration.OrderIntegrationTestFixture.사용자_주문_전체_조회_요청;
import static cart.integration.OrderIntegrationTestFixture.주문_상세_조회_요청;
import static cart.integration.OrderIntegrationTestFixture.주문_상품_응답;
import static cart.integration.OrderIntegrationTestFixture.주문_요청;
import static cart.integration.OrderIntegrationTestFixture.주문_전체_조회_응답_검증;
import static cart.integration.OrderIntegrationTestFixture.주문_조회_응답_검증;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import cart.domain.coupon.IssuableCoupon;
import cart.domain.coupon.MemberCoupon;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;


    private Member 밀리;
    private Member 박스터;

    private Product 상품_치킨;
    private Product 상품_피자;
    private Product 상품_햄버거;

    private CartItem 장바구니_밀리_치킨;
    private CartItem 장바구니_밀리_피자;
    private CartItem 장바구니_밀리_햄버거;

    private Coupon 쿠폰_10퍼센트;
    private Coupon 구매_증정_쿠폰;

    private MemberCoupon 밀리_쿠폰_10퍼센트;

    @BeforeEach
    void init() {
        밀리 = memberRepository.save(new Member("millie@email.com", "millie"));
        박스터 = memberRepository.save(new Member("boxster@email.com", "boxster"));

        상품_치킨 = productRepository.save(new Product("치킨", 10000, "http://chicken.com"));
        상품_피자 = productRepository.save(new Product("피자", 20000, "http://pizza.com"));
        상품_햄버거 = productRepository.save(new Product("햄버거", 7000, "http://hamburger.com"));

        장바구니_밀리_치킨 = cartItemRepository.save(new CartItem(상품_치킨, 밀리));
        장바구니_밀리_피자 = cartItemRepository.save(new CartItem(상품_피자, 밀리));
        장바구니_밀리_햄버거 = cartItemRepository.save(new CartItem(상품_햄버거, 밀리));

        쿠폰_10퍼센트 = couponRepository.save(
                new Coupon("10퍼센트 할인 쿠폰", CouponType.RATE, BigDecimal.valueOf(10), new Money(1000)));
        구매_증정_쿠폰 = couponRepository.save(
                new Coupon("구매 증정 쿠폰", CouponType.RATE, BigDecimal.valueOf(20), new Money(1000)));
        couponRepository.saveIssuableCoupon(new IssuableCoupon(구매_증정_쿠폰, new Money(15000)));
        밀리_쿠폰_10퍼센트 = memberCouponRepository.save(new MemberCoupon(밀리, 쿠폰_10퍼센트));
    }

    @Nested
    class 주문을_할_때 {

        @Test
        void 정상_주문한다() {
            var 응답 = 주문_요청(밀리, 3000, 밀리_쿠폰_10퍼센트.getId(), 30000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            응답_코드_검증(응답, HttpStatus.CREATED);
        }

        @Test
        void 다른_사용자_정보로_요청시_실패한다() {
            var 응답 = 주문_요청(박스터, 3000, 밀리_쿠폰_10퍼센트.getId(), 30000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            응답_코드_검증(응답, HttpStatus.FORBIDDEN);
        }

        @Test
        void 요청한_상품의_총_금액이_계산한_금액과_다르면_실패한다() {
            var 응답 = 주문_요청(밀리, 3000, 밀리_쿠폰_10퍼센트.getId(), 33001, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            응답_코드_검증(응답, HttpStatus.BAD_REQUEST);
        }

        @Test
        void 쿠폰을_사용하지_않으면_조건에_맞는_쿠폰을_발급한다() {
            var 응답 = 주문_요청(밀리, 3000, -1L, 33000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId());

            쿠폰_전체_조회_응답_검증(쿠폰_조회_요청(밀리), 쿠폰_전체_조회_응답(
                    List.of(정률_쿠폰(밀리_쿠폰_10퍼센트.getId(), "10퍼센트 할인 쿠폰", 10, LocalDate.now().plusDays(3), 1000),
                            정률_쿠폰(null, "구매 증정 쿠폰", 20, LocalDate.now().plusDays(3), 1000)),
                    List.of()
            ));
        }
    }

    @Nested
    class 주문을_상세_조회_할_때 {

        @Test
        void 정상_조회한다() {
            Long 주문_ID = 아이디를_반환한다(주문_요청(밀리, 3000, 밀리_쿠폰_10퍼센트.getId(), 30000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId()));

            var 응답 = 주문_상세_조회_요청(밀리, 주문_ID);

            응답_코드_검증(응답, HttpStatus.OK);
            주문_조회_응답_검증(응답, 주문_ID, 3000, "10퍼센트 할인 쿠폰", 3000, 30000, 30000, 주문_상품_응답(장바구니_밀리_치킨), 주문_상품_응답(장바구니_밀리_피자));
        }

        @Test
        void 다른_사용자가_요청_시_실패한다() {
            Long 주문_ID = 아이디를_반환한다(주문_요청(밀리, 3000, 밀리_쿠폰_10퍼센트.getId(), 30000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId()));

            var 응답 = 주문_상세_조회_요청(박스터, 주문_ID);

            응답_코드_검증(응답, HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    class 사용자_주문을_전체_조회할_때 {

        @Test
        void 정상_조회한다() {
            Long 첫번째_주문_ID = 아이디를_반환한다(
                    주문_요청(밀리, 3000, 밀리_쿠폰_10퍼센트.getId(), 30000, 장바구니_밀리_치킨.getId(), 장바구니_밀리_피자.getId()));
            Long 두번째_주문_ID = 아이디를_반환한다(주문_요청(밀리, 3000, -1L, 10000, 장바구니_밀리_햄버거.getId()));

            var 응답 = 사용자_주문_전체_조회_요청(밀리);

            응답_코드_검증(응답, HttpStatus.OK);
            주문_전체_조회_응답_검증(
                    응답,
                    사용자_주문_목록_응답(첫번째_주문_ID, 30000, 주문_상품_응답(장바구니_밀리_치킨), 주문_상품_응답(장바구니_밀리_피자)),
                    사용자_주문_목록_응답(두번째_주문_ID, 10000, 주문_상품_응답(장바구니_밀리_햄버거))
            );
        }
    }
}
