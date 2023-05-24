package cart.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.CartItemService;
import cart.dao.MemberDao;
import cart.dto.CartItemResponse;
import cart.dto.DtoSnippet;
import cart.dto.ProductResponse;
import cart.ui.CartItemApiController;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(CartItemApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CartItemApiControllerTest extends ControllerTestWithDocs {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartItemService cartItemService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberDao memberDao;

    @Test
    void 장바구니_항목_조회() throws Exception {
        ProductResponse productResponse = new ProductResponse(1L, "안성탕면", 1000, "www.naver.com");
        List<CartItemResponse> cartItemResponses = List.of(new CartItemResponse(1L, 23, productResponse));
        when(cartItemService.findByMember(any())).thenReturn(
                cartItemResponses);
        //when
        ResultActions result = mockMvc.perform(get("/cart-items"));

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentationOf("product-delete",
                        DtoSnippet.of(cartItemResponses)
                ));
    }
}
