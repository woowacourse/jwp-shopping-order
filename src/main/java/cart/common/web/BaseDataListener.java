package cart.common.web;

import cart.coupon.application.CouponRepository;
import cart.coupon.application.CouponService;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;
import cart.member.Member;
import cart.member.application.MemberRepository;
import cart.product.Product;
import cart.product.application.ProductRepository;
import cart.sale.SaleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("production")
public class BaseDataListener implements ApplicationListener<ContextRefreshedEvent> {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final DiscountPolicyService discountPolicyService;
    private final SaleRepository saleRepository;

    public BaseDataListener(MemberRepository memberRepository, ProductRepository productRepository, CouponRepository couponRepository, DiscountPolicyService discountPolicyService, SaleRepository saleRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.discountPolicyService = discountPolicyService;
        this.saleRepository = saleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final var memberId = memberRepository.addMember(new Member(null, "a@a.gmail.com", "1234"));
        final var member2Id = memberRepository.addMember(new Member(null, "b@b.gmail.com", "1234"));
        final var member = memberRepository.getMemberById(memberId);

        final var chickenId = productRepository.createProduct(new Product("치킨", 10_000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        productRepository.createProduct(new Product("샐러드", 20000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        productRepository.createProduct(new Product("피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));
        productRepository.createProduct(new Product("보쌈", 20000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80"));

        final var tenPercentForAllDiscountPolicyId = discountPolicyService.savePolicy(DiscountCondition.from(DiscountTarget.ALL, DiscountUnit.PERCENTAGE, 10));
        final var tenPercentForAllDiscountCouponId = couponRepository.save("전체 10% 할인 쿠폰", tenPercentForAllDiscountPolicyId);

        final var twentyPercentForAllDiscountPolicyId = discountPolicyService.savePolicy(DiscountCondition.from(DiscountTarget.ALL, DiscountUnit.PERCENTAGE, 20));
        final var twentyPercentForAllDiscountCouponId = couponRepository.save("전체 20% 할인 쿠폰", twentyPercentForAllDiscountPolicyId);

        final var freeDeliveryPolicyId = discountPolicyService.savePolicy(DiscountCondition.from(DiscountTarget.DELIVERY, DiscountUnit.PERCENTAGE, 100));
        final var freeDeliveryCouponId = couponRepository.save("배송비 무료 쿠폰", freeDeliveryPolicyId);

        final var chicken3000DiscountId = discountPolicyService.savePolicy(DiscountCondition.makeConditionForSpecificProducts(List.of(chickenId), DiscountUnit.ABSOLUTE, 3000));
        saleRepository.save("치킨만 3,000원 할인", chicken3000DiscountId);

        memberRepository.addCoupon(member, tenPercentForAllDiscountCouponId);
        memberRepository.addCoupon(member, twentyPercentForAllDiscountCouponId);
        memberRepository.addCoupon(member, freeDeliveryCouponId);
    }
}
