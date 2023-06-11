package cart.ui;

import cart.dao.MemberDao;
import cart.dto.SavingPointResponse;
import cart.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
public class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    private PointService pointService;

    @DisplayName("쿼리 totalPrice 금액을 음수로 입력하면 예외가 발생한다")
    @Test
    public void invalidTotalPrice() throws Exception {
        Long invalidTotalPrice = -100L;
        String expectedResponse = "[\"총 금액은 0 이상으로 입력해주세요\"]";

        mockMvc.perform(get("/saving-point")
                        .param("totalPrice", invalidTotalPrice.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
    }

    @DisplayName("적립 포인트를 조회한다")
    @Test
    public void getSavingPoint() throws Exception {
        Long validTotalPrice = 500L;

        SavingPointResponse savingPointResponse = new SavingPointResponse(5L);
        when(pointService.findSavingPoints(validTotalPrice)).thenReturn(savingPointResponse);

        mockMvc.perform(get("/saving-point")
                        .param("totalPrice", validTotalPrice.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.savingPoint").value(5));
    }
}
