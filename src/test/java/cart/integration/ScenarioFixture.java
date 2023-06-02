package cart.integration;

import cart.cartitem.CartItem;
import cart.cartitem.application.CartItemRepository;
import cart.cartitem.application.CartItemService;
import cart.cartitem.presentation.dto.CartItemRequest;
import cart.coupon.Coupon;
import cart.coupon.application.CouponRepository;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;
import cart.member.application.MemberRepository;
import cart.member.Member;
import cart.product.Product;
import cart.product.application.ProductRepository;
import cart.sale.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ScenarioFixture extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private DiscountPolicyService discountPolicyService;

    protected Product 치킨;
    protected Product 샐러드;
    protected Product 피자;
    protected Product 보쌈;

    protected CartItem 치킨장바구니;
    protected CartItem 샐러드장바구니;
    protected CartItem 피자장바구니;

    protected Coupon 전체10프로할인쿠폰;
    protected Coupon 전체20프로할인쿠폰;
    protected Coupon 배송비무료쿠폰;

    protected Member 사용자1;

    @BeforeEach
    void setUp() {
        super.setUp();

        상품_만들기();
        쿠폰_만들기();
        사용자_만들기();
        할인_만들기();

        사용자가_장바구니에_상품_담게하기();
        사용자에게_쿠폰_발급하기();
    }

    private void 사용자가_장바구니에_상품_담게하기() {
        final var 치킨장바구니아이디 = cartItemService.add(사용자1, new CartItemRequest(치킨.getId()));
        치킨장바구니 = cartItemRepository.findById(치킨장바구니아이디);
        final var 샐러드장바구니아이디 = cartItemService.add(사용자1, new CartItemRequest(샐러드.getId()));
        샐러드장바구니 = cartItemRepository.findById(샐러드장바구니아이디);
        final var 피자장바구니아이디 = cartItemService.add(사용자1, new CartItemRequest(피자.getId()));
        피자장바구니 = cartItemRepository.findById(피자장바구니아이디);
    }

    private void 사용자에게_쿠폰_발급하기() {
        memberRepository.addCoupon(사용자1, 전체10프로할인쿠폰.getId());
        memberRepository.addCoupon(사용자1, 전체20프로할인쿠폰.getId());
        memberRepository.addCoupon(사용자1, 배송비무료쿠폰.getId());
    }

    private void 할인_만들기() {
        final var 치킨만3000원할인아이디 = discountPolicyService.savePolicy(DiscountCondition.makeConditionForSpecificProducts(List.of(치킨.getId()), DiscountUnit.ABSOLUTE, 3000));
        saleRepository.save("치킨만 3,000원 할인", 치킨만3000원할인아이디);
    }

    private void 사용자_만들기() {
        final var 사용자1아이디 = memberRepository.addMember(new Member(null, "a@a.gmail.com", "1234"));
        사용자1 = memberRepository.getMemberById(사용자1아이디);
    }

    private void 쿠폰_만들기() {
        final var 전체10프로할인아이디 = discountPolicyService.savePolicy(DiscountCondition.from(DiscountTarget.ALL, DiscountUnit.PERCENTAGE, 10));
        final var 전체10프로할인쿠폰아이디 = couponRepository.save("전체 10% 할인 쿠폰", 전체10프로할인아이디);
        전체10프로할인쿠폰 = couponRepository.findById(전체10프로할인쿠폰아이디);

        final var 전체20프로할인아이디 = discountPolicyService.savePolicy(DiscountCondition.from(DiscountTarget.ALL, DiscountUnit.PERCENTAGE, 20));
        final var 전체20프로할인쿠폰아이디 = couponRepository.save("전체 20% 할인 쿠폰", 전체20프로할인아이디);
        전체20프로할인쿠폰 = couponRepository.findById(전체20프로할인쿠폰아이디);

        final var 배송비무료아이디 = discountPolicyService.savePolicy(DiscountCondition.from(DiscountTarget.DELIVERY, DiscountUnit.PERCENTAGE, 100));
        final var 배송비무료쿠폰아이디 = couponRepository.save("배송비 무료 쿠폰", 배송비무료아이디);
        배송비무료쿠폰 = couponRepository.findById(배송비무료쿠폰아이디);
    }

    private Long 상품_만들기() {
        final var 치킨아이디 = productRepository.createProduct(new Product("치킨", 10_000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        치킨 = new Product(치킨아이디, "치킨", 10_000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

        final var 샐러드아이디 = productRepository.createProduct(new Product("샐러드", 20000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        샐러드 = new Product(샐러드아이디, "샐러드", 20000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

        final var 피자아이디 = productRepository.createProduct(new Product("피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        피자 = new Product(피자아이디, "피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

        final var 보쌈아이디 = productRepository.createProduct(new Product("보쌈", 20000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        보쌈 = new Product(보쌈아이디, "보쌈", 20000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");
        return 치킨아이디;
    }
}
