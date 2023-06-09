package cart.ui;

import cart.dao.MemberDao;
import cart.service.CartItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class)
class CartItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    private CartItemService cartItemService;

    @Test
    @DisplayName("장바구니 물품 업데이트 PathVariable 유효성 검사 테스트")
    public void validatePathVariable_Patch() throws Exception {
        Long invalidId = 0L;
        String requestContent = "{ \"quantity\" : 4 }";
        String expectedResponse = "[\"ID는 1 이상의 정수로 입력해주세요\"]";

        mockMvc.perform(patch("/cart-items/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
        ;
    }

    @Test
    @DisplayName("장바구니 물품 삭제 PathVariable 유효성 검사 테스트")
    public void validatePathVariable_Delete() throws Exception {
        Long invalidId = 0L;
        String expectedResponse = "[\"ID는 1 이상의 정수로 입력해주세요\"]";

        mockMvc.perform(delete("/cart-items/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, true));
    }
}
