package cart.controller;

import cart.application.CartItemService;
import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.ui.CartItemApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.ShoppingOrderFixture.member1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(CartItemApiController.class)
class CartItemApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private MemberDao memberDao;

    @DisplayName("사용자의 장바구니 아이템 목록을 보여준다")
    @Test
    void showCartItems() throws Exception {
        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(member1);
        when(cartItemService.findByMember(any()))
                .thenReturn(List.of(CartItemResponse.of(new CartItem(1L, 2L, new Product(1L, "치킨", 10000, "imageUrl", 10.0, true), member1))));

        this.mockMvc.perform(get("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].quantity").exists())
                .andExpect(jsonPath("$[0].product.id").exists())
                .andExpect(jsonPath("$[0].product.name").exists())
                .andExpect(jsonPath("$[0].product.imageUrl").exists())
                .andExpect(jsonPath("$[0].product.pointRatio").exists())
                .andExpect(jsonPath("$[0].product.pointAvailable").exists());
    }

    @DisplayName("장바구니에 상품을 추가한다")
    @Test
    void addCartItems() throws Exception {
        String body = objectMapper.writeValueAsString(
                new CartItemRequest(1L));

        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(member1);
        when(cartItemService.add(any(), any())).thenReturn(1L);

        this.mockMvc.perform(post("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE=")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cart-items/1"));
    }

    @DisplayName("카트에 담긴 상품의 수량을 변경한다")
    @Test
    void updateCartItemQuantity() throws Exception {
        String body = objectMapper.writeValueAsString(
                new CartItemQuantityUpdateRequest(2));

        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(member1);

        this.mockMvc.perform(patch("/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE=")
                        .content(body))
                .andExpect(status().isOk());
    }

    @DisplayName("장바구니를 삭제한다")
    @Test
    void removeCartItems() throws Exception {
        when(memberDao.getMemberByEmail("a@a.com")).thenReturn(member1);

        this.mockMvc.perform(delete("/cart-items/1")
                        .header("Authorization", "Basic YUBhLmNvbTpwYXNzd29yZDE="))
                .andExpect(status().isNoContent());
    }
}
