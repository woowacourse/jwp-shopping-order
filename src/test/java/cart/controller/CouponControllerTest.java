package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dto.CouponSaveRequest;
import cart.entity.CouponEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void 쿠폰을_추가한다() throws Exception {
        // given
        final CouponSaveRequest couponSaveRequest = new CouponSaveRequest("1000원이상 결제시 1원쿠폰", "PRICE", 1L, 1000L);
        final String request = objectMapper.writeValueAsString(couponSaveRequest);

        // when
        MvcResult mvcResult = mockMvc.perform(post("/coupons")
                        .content(request)
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
            long discountValue = rs.getLong("discount_value");
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
}
