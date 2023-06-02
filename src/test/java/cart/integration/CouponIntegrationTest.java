package cart.integration;

import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.MEMBER_1_AUTH_HEADER;
import static cart.TestDataFixture.OBJECT_MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.TestDataFixture;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.service.response.CouponResponse;
import cart.service.response.DiscountPriceResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class CouponIntegrationTest extends IntegrationTest {

    @DisplayName("멤버가 보유한 모든 멤버쿠폰들을 조회한다.")
    @Test
    void findMemberCoupons() throws Exception {
        final Coupon coupon = couponRepository.insert(TestDataFixture.DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);

        final MvcResult result = mockMvc
                .perform(get("/coupon")
                        .header("Authorization", MEMBER_1_AUTH_HEADER))
                .andExpect(status().isOk())
                .andReturn();

        final String resultJsonString = result.getResponse().getContentAsString();
        final List<CouponResponse> responses = OBJECT_MAPPER.readValue(resultJsonString, new TypeReference<>() {
        });

        assertThat(responses)
                .extracting(CouponResponse::getName, CouponResponse::getId)
                .containsExactly(tuple(coupon.getName(), savedMemberCoupon.getId()));
    }

    @DisplayName("멤버쿠폰과 원가격으로 할인 가격과 총 가격을 계산한다.")
    @Test
    void discountPriceCoupon() throws Exception {
        final Coupon coupon = couponRepository.insert(TestDataFixture.DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);

        final MvcResult result = mockMvc
                .perform(get("/coupon/discount")
                        .param("total-price", "10000")
                        .param("coupon-id", String.valueOf(savedMemberCoupon.getId()))
                        .header("Authorization", MEMBER_1_AUTH_HEADER))
                .andExpect(status().isOk())
                .andReturn();

        final String resultJsonString = result.getResponse().getContentAsString();
        final DiscountPriceResponse response = OBJECT_MAPPER.readValue(resultJsonString, DiscountPriceResponse.class);

        assertThat(response)
                .extracting(DiscountPriceResponse::getDiscountPrice, DiscountPriceResponse::getTotalPrice)
                .containsExactly(-5000, 5000);
    }
}
