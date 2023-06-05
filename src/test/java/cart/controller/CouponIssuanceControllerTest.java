package cart.controller;

import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.member.Member;
import cart.dto.coupon.CouponSaveRequest;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CouponIssuanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 모든_사용자에게_쿠폰을_발급한다() throws Exception {
        // given
        final Member member1 = memberRepository.save(사용자1);
        final Member member2 = memberRepository.save(사용자2);
        final CouponSaveRequest couponSaveRequest = new CouponSaveRequest("배달비 할인 쿠폰", "delivery", 3000L, 0L);
        final String request = objectMapper.writeValueAsString(couponSaveRequest);

        // expect
        mockMvc.perform(post("/issuance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertAll(
                () -> assertThat(couponRepository.findAllByUsedAndMemberId(false, member1.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByUsedAndMemberId(false, member2.getId())).hasSize(1)
        );
    }
}
