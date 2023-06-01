package cart.acceptance.coupon;

import static cart.acceptance.common.CommonAcceptanceSteps.생성된_ID;
import static cart.acceptance.coupon.CouponSteps.특정_회원의_쿠폰_전체_조회_요청;
import static cart.acceptance.product.ProductSteps.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import cart.acceptance.AcceptanceTest;
import cart.coupon.domain.DiscountType;
import cart.coupon.domain.TargetType;
import cart.coupon.infrastructure.dao.CouponDao;
import cart.coupon.infrastructure.entity.CouponEntity;
import cart.coupon.presentation.dto.AllCouponQueryResponse;
import cart.coupon.presentation.dto.GeneralCouponResponse;
import cart.coupon.presentation.dto.SpecificCouponResponse;
import cart.member.domain.Member;
import cart.member.infrastructure.persistence.dao.MemberDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Coupon 통합 테스트")
public class CouponAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CouponDao couponDao;

    private Member 회원1;
    private Member 회원2;

    Long productId1;
    Long productId2;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        memberDao.addMember(new Member(null, "email1", "1234"));
        memberDao.addMember(new Member(null, "email2", "4321"));
        회원1 = memberDao.getMemberById(1L);
        회원2 = memberDao.getMemberById(2L);

        productId1 = 생성된_ID(상품_생성_요청("코코닥", 10000, "image1"));
        productId2 = 생성된_ID(상품_생성_요청("말랑", 20000, "image2"));

        couponDao.save(new CouponEntity(1L,
                "전체 비율 할인 쿠폰",
                회원1.getId(),
                DiscountType.RATE,
                TargetType.ALL,
                null,
                50));
        couponDao.save(new CouponEntity(2L,
                "전체 고정 할인 쿠폰",
                회원1.getId(),
                DiscountType.FIX,
                TargetType.ALL,
                null,
                1000));
        couponDao.save(new CouponEntity(3L,
                "특정 비율 할인 쿠폰",
                회원1.getId(),
                DiscountType.RATE,
                TargetType.SPECIFIC,
                productId1,
                100));
        couponDao.save(new CouponEntity(4L,
                "특정 고정 할인 쿠폰",
                회원1.getId(),
                DiscountType.FIX,
                TargetType.SPECIFIC,
                productId2,
                2000));

        couponDao.save(new CouponEntity(5L,
                "다른 회원의 쿠폰",
                회원2.getId(),
                DiscountType.RATE,
                TargetType.ALL,
                null,
                50));
    }

    @Test
    void 특정_회원의_모든_쿠폰을_조회한다() {
        // when
        var 응답 = 특정_회원의_쿠폰_전체_조회_요청(회원1);

        AllCouponQueryResponse actual = 응답.as(AllCouponQueryResponse.class);

        // then
        AllCouponQueryResponse expected = new AllCouponQueryResponse(
                List.of(
                        new GeneralCouponResponse(1L,
                                "전체 비율 할인 쿠폰",
                                회원1.getId(),
                                DiscountType.RATE,
                                TargetType.ALL,
                                50),
                        new GeneralCouponResponse(2L,
                                "전체 고정 할인 쿠폰",
                                회원1.getId(),
                                DiscountType.FIX,
                                TargetType.ALL,
                                1000)
                ),
                List.of(
                        new SpecificCouponResponse(3L,
                                "특정 비율 할인 쿠폰",
                                회원1.getId(),
                                DiscountType.RATE,
                                TargetType.SPECIFIC,
                                100,
                                productId1),
                        new SpecificCouponResponse(4L,
                                "특정 고정 할인 쿠폰",
                                회원1.getId(),
                                DiscountType.FIX,
                                TargetType.SPECIFIC,
                                2000,
                                productId2)
                )
        );
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
