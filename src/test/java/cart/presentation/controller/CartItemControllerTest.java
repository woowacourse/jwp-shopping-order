package cart.presentation.controller;

import cart.WebMvcConfig;
import cart.application.service.CartItemService;
import cart.presentation.dto.request.CartItemQuantityRequest;
import cart.presentation.dto.request.CartItemRequest;
import cart.presentation.dto.response.CartItemResponse;
import cart.presentation.dto.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CartItemController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class, HandlerMethodArgumentResolver.class}))
class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CartItemService cartItemService;

    private CartItemResponse cartItemResponse;

    @BeforeEach
    void setup() {
        cartItemResponse = new CartItemResponse(1L, 1L,
                new ProductResponse(1L, "피자", 1000L, "https://", 5.0, false));

    }

    @Test
    @DisplayName("모든 장바구니 품목을 조회할 수 있다")
    void showCartItems() throws Exception {
        // given
        when(cartItemService.getAllCartItems(any())).thenReturn(List.of(cartItemResponse));
        // when
        mockMvc.perform(get("/cart-items"))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].quantity").exists())
                .andExpect(jsonPath("$[*].product.id").exists())
                .andExpect(jsonPath("$[*].product.name").exists())
                .andExpect(jsonPath("$[*].product.price").exists())
                .andExpect(jsonPath("$[*].product.imageUrl").exists())
                .andExpect(jsonPath("$[*].product.pointRatio").exists())
                .andExpect(jsonPath("$[*].product.pointAvailable").exists());
    }

    @Test
    @DisplayName("장바구니 품목을 추가할 수 있다")
    void addCartItems() throws Exception {
        // given
        CartItemRequest request = new CartItemRequest(1L);
        String body = objectMapper.writeValueAsString(request);
        when(cartItemService.createCartItem(any(), any())).thenReturn(1L);
        // when
        mockMvc.perform(post("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                // then
                .andExpect(header().string("Location", "/cart-items/1"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("장바구니 품목 수량을 변경할 수 있다")
    void updateCartItemQuantity() throws Exception {
        // given
        CartItemQuantityRequest request = new CartItemQuantityRequest();
        String body = objectMapper.writeValueAsString(request);
        doNothing().when(cartItemService).updateQuantity(any(), any(), any());
        // when
        mockMvc.perform(patch("/cart-items/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 품목을 삭제할 수 있다")
    void removeCartItems() throws Exception {
        // given
        doNothing().when(cartItemService).remove(any(), any());
        // when
        mockMvc.perform(delete("/cart-items/{id}", 1L))
                // then
                .andExpect(status().isNoContent());
    }
}
