package cart.controller;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class MemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 사용자의_쿠폰을_조회한다() throws Exception {
        // given
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Coupon coupon = couponRepository.save(new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), 3000L, new Money(30000L)));
        memberCouponRepository.save(new MemberCoupon(member, coupon, false));
        memberCouponRepository.save(new MemberCoupon(member, coupon, false));

        // expect
        mockMvc.perform(get("/coupons")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }
}