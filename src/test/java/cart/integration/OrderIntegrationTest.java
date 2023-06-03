package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.application.dto.ProductRequest;
import cart.application.dto.coupon.CreateCouponRequest;
import cart.application.dto.coupon.IssueCouponRequest;
import cart.application.dto.order.FindOrderCouponResponse;
import cart.application.dto.order.FindOrderCouponsResponse;
import cart.domain.Member;
import cart.domain.coupon.CouponType;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member other;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        long memberId = memberDao.create(new MemberEntity("test@test.com", "test!", "testNickname"));
        long otherId = memberDao.create(new MemberEntity("other@test.com", "test!", "otherNickname"));
        member = new Member(memberId, "test@test.com", "test!", "testNickname");
        other = new Member(otherId, "other@test.com", "test!", "otherNickname");
    }

    @Test
    void 장바구니_아이디로_사용자_쿠폰을_조회한다() {
        Long cartItemId1 = requestAddCartItemAndGetId(member, productId);
        Long cartItemId2 = requestAddCartItemAndGetId(member, productId2);

        LocalDateTime pastDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(9999, 12, 31, 0, 0);

        long couponId1 = createCoupon(
                new CreateCouponRequest("10%(10,000~)", 10_000, 3000, CouponType.FIXED_PERCENTAGE, 10));
        long couponId2 = createCoupon(
                new CreateCouponRequest("1000원(50,000~)", 50_000, 1000, CouponType.FIXED_AMOUNT, 10_000));

        long memberCouponId1 = issueCoupon(member, couponId1, new IssueCouponRequest(futureDate));
        long memberCouponId2 = issueCoupon(member, couponId2, new IssueCouponRequest(futureDate));
        long memberCouponId3 = issueCoupon(member, couponId2, new IssueCouponRequest(futureDate));
        issueCoupon(member, couponId2, new IssueCouponRequest(pastDate));
        issueCoupon(other, couponId2, new IssueCouponRequest(futureDate));
        memberCouponDao.changeCouponStatus(memberCouponId3, true);

        FindOrderCouponsResponse response = findCouponsByCartItemIds(member, List.of(cartItemId1, cartItemId2));

        List<FindOrderCouponResponse> expected = List.of(
                new FindOrderCouponResponse(memberCouponId1, "10%(10,000~)", 10_000, 3000, true, 2500, futureDate),
                new FindOrderCouponResponse(memberCouponId2, "1000원(50,000~)", 50_000, 1000, false, null, futureDate)
        );

        assertThat(response.getCoupons())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }
}
