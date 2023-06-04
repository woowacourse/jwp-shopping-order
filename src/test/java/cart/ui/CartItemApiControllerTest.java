package cart.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.CartItemService;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import cart.dto.ProductResponse;
import cart.entity.MemberEntity;
import cart.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

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

    private String basicAuthHeader;

    @BeforeEach
    void setUp() {
        basicAuthHeader = "Basic " + Base64Utils.encodeToString("id:password".getBytes());

        given(memberDao.findByEmail("id"))
                .willReturn(Optional.of(new MemberEntity(1L, "id", "password")));
    }

    @Test
    @DisplayName("사용자별 장바구니 정보를 조회한다.")
    void showCartItems() throws Exception {
        final ProductResponse chicken = new ProductResponse(1L, "chicken", 3000, "example.com/chicken.jpg");
        final ProductResponse pizza = new ProductResponse(2L, "pizza", 5000, "example.com/pizza.jpg");
        final CartItemResponse chickenResponse = new CartItemResponse(1L, 3, chicken);
        final CartItemResponse pizzaResponse = new CartItemResponse(2L, 2, pizza);
        final List<CartItemResponse> expected = List.of(chickenResponse, pizzaResponse);

        given(cartItemService.findByMember(any(Member.class))).willReturn(expected);

        final MvcResult mvcResult = mockMvc.perform(get("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        List<CartItemResponse> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                }
        );

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).usingRecursiveComparison().ignoringFields("product").isEqualTo(expected),
                () -> assertThat(actual).extracting(CartItemResponse::getProduct)
                        .usingRecursiveComparison().isEqualTo(List.of(chicken, pizza))
        );
    }

    @Test
    @DisplayName("사용자의 장바구니 아이템을 추가한다.")
    void addCartItems() throws Exception {
        final CartItemRequest cartItemRequest = new CartItemRequest(1L);
        given(cartItemService.save(any(Member.class), any(CartItemRequest.class))).willReturn(1L);

        mockMvc.perform(post("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/cart-items/1"));
    }

    @Test
    @DisplayName("장바구니 아이템의 수량을 변경한다.")
    void updateCartItemQuantity() throws Exception {
        final CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest = new CartItemQuantityUpdateRequest(1);
        willDoNothing().given(cartItemService)
                .updateQuantity(any(Member.class), eq(1L), any(CartItemQuantityUpdateRequest.class));

        mockMvc.perform(patch("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemQuantityUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니의 아이템을 삭제한다.")
    void removeCartItems() throws Exception {
        willDoNothing().given(cartItemService).deleteById(any(Member.class), eq(1L));

        mockMvc.perform(delete("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("삭제할 아이템 정보가 없을 시 404 상태를 응답한다.")
    void removeCartItemsWithInvalidId() throws Exception {
        willThrow(new ResourceNotFoundException("상품 정보를 찾을 수 없습니다.")).given(cartItemService)
                .deleteById(any(Member.class), eq(Long.MAX_VALUE));

        mockMvc.perform(delete("/cart-items/{id}", Long.MAX_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
