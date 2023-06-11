package cart.ui;

import cart.dao.MemberDao;
import cart.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    private OrderService orderService;

    @DisplayName("Get 요청 - 유효하지 않은 ID 요청 시 PathVariable 유효성 검증 실패 확인")
    @Test
    public void validatePathVariable_Get() throws Exception {
        Long invalidId = 0L;
        String expectedResponse = "[\"ID는 1 이상의 정수로 입력해주세요\"]";

        mockMvc.perform(get("/orders/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
    }
}
