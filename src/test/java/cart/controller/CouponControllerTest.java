package cart.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dao.MemberCouponDao;
import cart.domain.cart.Member;
import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeliveryFeeDiscountPolicy;
import cart.domain.coupon.MinimumPriceDiscountCondition;
import cart.domain.coupon.NoneDiscountCondition;
import cart.entity.MemberCouponEntity;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    void 사용자의_모든_쿠폰을_조회한다() throws Exception {
        // given
        final Coupon coupon1 = couponRepository.save(new Coupon(
                "2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        final Coupon coupon2 = couponRepository.save(new Coupon(
                "30000원 이상 배달비 할인 쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new MinimumPriceDiscountCondition(30000)
        ));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        memberCouponDao.insert(new MemberCouponEntity(coupon1.getId(), member.getId(), false));
        memberCouponDao.insert(new MemberCouponEntity(coupon2.getId(), member.getId(), false));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        // expect
        mockMvc.perform(get("/coupons")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }
}
