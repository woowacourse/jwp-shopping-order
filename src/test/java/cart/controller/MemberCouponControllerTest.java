package cart.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.policy.PriceDiscountPolicy;
import cart.entity.MemberCouponEntity;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class MemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("coupon_id", "member_id", "used")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 해당멤버의_모든_쿠폰을_조회한다() throws Exception {
        // given
        final Member savedMember = memberRepository.save(new Member("wuga@naver.com", "1234"));
        final Coupon savedCoupon = couponRepository.saveCoupon(
                new Coupon("1000원이상 결제시 1원쿠폰", new PriceDiscountPolicy(1L), 1000L));

        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(savedCoupon.getId(), savedMember.getId(),
                false);
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(memberCouponEntity);
        final int savedMemberCouponId = jdbcInsert.executeAndReturnKey(parameterSource).intValue();

        final String header = "Basic " + new String(Base64.getEncoder().encode("wuga@naver.com:1234".getBytes()));

        // when
        mockMvc.perform(get("/member-coupons")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(savedMemberCouponId)))
                .andDo(print())
                .andReturn();
    }
}
