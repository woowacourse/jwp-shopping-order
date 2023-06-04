package cart.controller;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.CouponFixture.쿠폰_발급;
import static cart.fixture.MemberFixture.사용자1;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.member.Member;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
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

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 사용자의_모든_쿠폰을_조회한다() throws Exception {
        // given
        final Member member = memberRepository.save(사용자1);
        couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));
        couponRepository.save(쿠폰_발급(_3만원_이상_배달비_3천원_할인_쿠폰, member.getId()));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));

        // expect
        mockMvc.perform(get("/coupons")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }
}
