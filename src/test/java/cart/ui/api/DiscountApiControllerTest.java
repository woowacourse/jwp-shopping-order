package cart.ui.api;

import cart.application.DiscountService;
import cart.dao.MemberDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiscountApiController.class)
class DiscountApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberDao memberDao;
    @MockBean
    private DiscountService discountService;

    @DisplayName("금액과 회원 등급으로 할인율을 조회할 수 있다")
    @Test
    void getDiscountInfo() throws Exception {
        mockMvc.perform(
                        get("/discount").queryParam("price", "1000").queryParam("memberGrade", "gold"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("없는 회원 등급을 입력하면 400이 리턴된다")
    @Test
    void getDiscountInfo_fail() throws Exception {
        mockMvc.perform(
                        get("/discount").param("price", "1000").param("memberGrade", "gold2"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
