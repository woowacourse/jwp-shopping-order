package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.policy.PriceDiscountPolicy;
import cart.dto.CouponSaveRequest;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("coupon_id", "member_id", "used")
                .usingGeneratedKeyColumns("id");
    }


    @Test
    void 쿠폰을_추가한다() throws Exception {
        // given
        memberRepository.save(new Member("wuga@naver.com", "1234"));

        final String header = "Basic " + new String(Base64.getEncoder().encode("wuga@naver.com:1234".getBytes()));

        final CouponSaveRequest couponSaveRequest = new CouponSaveRequest("1000원이상 결제시 1원쿠폰", "PRICE", 1L, 1000L);
        final String request = objectMapper.writeValueAsString(couponSaveRequest);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/coupons")
                        .content(request)
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        String uri = mvcResult.getResponse().getHeader("Location");
        String[] split = uri.split("/");
        String savedCouponId = split[split.length - 1];

        // then
        List<CouponEntity> result = jdbcTemplate.query("SELECT * FROM coupon where id = ?", (rs, rowNum) -> {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            String policyType = rs.getString("policy_type");
            long discountValue = rs.getLong("discount_price");
            long minimumPrice = rs.getLong("minimum_price");
            return new CouponEntity(id, name, policyType, discountValue, minimumPrice);
        }, savedCouponId);

        assertThat(result).hasSize(1);
        assertThat(result).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(new CouponEntity(
                        couponSaveRequest.getName(),
                        couponSaveRequest.getDiscountPolicyType(),
                        couponSaveRequest.getDiscountValue(),
                        couponSaveRequest.getMinimumPrice()
                )));
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
        mockMvc.perform(get("/coupons")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(savedMemberCouponId)))
                .andDo(print())
                .andReturn();
    }
}
