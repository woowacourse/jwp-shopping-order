package cart.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.application.CartItemService;
import cart.dao.MemberDao;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.exception.CartItemNotFoundException;
import cart.exception.ProductNotFoundException;
import cart.fixture.CartItemResponseFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void addCartItem() throws Exception {
        final String request = objectMapper.writeValueAsString(new CartItemRequest(1L));
        given(cartItemService.addCartItem(any(), any())).willReturn(CartItemResponseFixture.CHICKEN);

        mockMvc.perform(post("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cart-items/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.product.id", is(1)))
                .andExpect(jsonPath("$.product.name", is("치킨")))
                .andExpect(jsonPath("$.product.price", is(10_000)))
                .andExpect(jsonPath("$.product.imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void addCartItemWithNoExistProductId() throws Exception {
        final String request = objectMapper.writeValueAsString(new CartItemRequest(1L));
        given(cartItemService.addCartItem(any(), any())).willThrow(new ProductNotFoundException(1L));

        mockMvc.perform(post("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("id: 1 에 해당하는 상품을 찾을 수 없습니다.")));
    }

    @Test
    void getCartItems() throws Exception {
        given(cartItemService.findByMember(any())).willReturn(List.of(
                CartItemResponseFixture.CHICKEN,
                CartItemResponseFixture.PIZZA
        ));

        mockMvc.perform(get("/cart-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].product.id", is(1)))
                .andExpect(jsonPath("$[0].product.name", is("치킨")))
                .andExpect(jsonPath("$[0].product.price", is(10_000)))
                .andExpect(jsonPath("$[0].product.imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$[0].quantity", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].product.id", is(2)))
                .andExpect(jsonPath("$[1].product.name", is("피자")))
                .andExpect(jsonPath("$[1].product.price", is(15_000)))
                .andExpect(jsonPath("$[1].product.imageUrl", is("http://example.com/pizza.jpg")))
                .andExpect(jsonPath("$[1].quantity", is(10)));
    }

    @Test
    void updateCartItemQuantity() throws Exception {
        final String request = objectMapper.writeValueAsString(new CartItemQuantityUpdateRequest(20));
        doNothing().when(cartItemService).updateQuantity(any(), anyLong(), any());

        mockMvc.perform(patch("/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    void updateCartItemQuantityWithNoExistId() throws Exception {
        final String request = objectMapper.writeValueAsString(new CartItemQuantityUpdateRequest(20));
        doThrow(new CartItemNotFoundException(1L)).when(cartItemService).updateQuantity(any(), anyLong(), any());

        mockMvc.perform(patch("/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("id: 1 에 해당하는 장바구니 품목을 찾을 수 없습니다.")));
    }

    @Test
    void deleteCartItem() throws Exception {
        doNothing().when(cartItemService).deleteCartItem(any(), anyLong());

        mockMvc.perform(delete("/cart-items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCartItemWithNoExistId() throws Exception {
        doThrow(new CartItemNotFoundException(1L)).when(cartItemService).deleteCartItem(any(), anyLong());

        mockMvc.perform(delete("/cart-items/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("id: 1 에 해당하는 장바구니 품목을 찾을 수 없습니다.")));
    }
}
