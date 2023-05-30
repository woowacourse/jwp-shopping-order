package cart.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.CartItemService;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.test.ControllerTest;
import cart.ui.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.controller.dto.request.CartItemRequest;
import cart.ui.controller.dto.response.CartItemResponse;
import cart.ui.controller.dto.response.MemberResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

@WebMvcTest(CartItemApiController.class)
class CartItemApiControllerTest extends ControllerTest {

    @MockBean
    private CartItemService cartItemService;

    @Test
    @DisplayName("showCartItems 메서드는 멤버의 장바구니 상품 목록을 응답한다.")
    void showCartItems() throws Exception {
        Member member = new Member(1L, "a@a.com", "password1", 10);
        Product productA = new Product(1L, "치킨", 10000, "http://chicken.com");
        Product productB = new Product(2L, "피자", 13000, "http://pizza.com");
        List<CartItemResponse> response = List.of(
                CartItemResponse.from(new CartItem(member, productA)),
                CartItemResponse.from(new CartItem(member, productB))
        );
        given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
        given(cartItemService.findByMember(any(Member.class))).willReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/cart-items")
                        .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CartItemResponse> result = objectMapper.readValue(jsonResponse, new TypeReference<List<CartItemResponse>>() {
        });
        assertThat(result).usingRecursiveComparison().isEqualTo(response);
    }

    @Nested
    @DisplayName("addCartItems 메서드는 ")
    class AddCartItems {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            mockMvc.perform(post("/cart-items"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @Test
        @DisplayName("상품 ID가 존재하지 않으면 400 상태를 반환한다.")
        void notExistProductId() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItemRequest request = new CartItemRequest(null);
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));

            mockMvc.perform(post("/cart-items")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: productId] 장바구니 등록을 위해 상품 ID는 필수입니다.]"));
        }

        @Test
        @DisplayName("유효한 요청이라면 장바구니 상품을 추가한다.")
        void addCartItems() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItemRequest request = new CartItemRequest(1L);
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
            given(cartItemService.add(any(Member.class), any(CartItemRequest.class))).willReturn(1L);

            mockMvc.perform(post("/cart-items")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/cart-items/" + 1));
        }
    }

    @Nested
    @DisplayName("updateCartItemQuantity 메서드는 ")
    class UpdateCartItemQuantity {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(5);

            mockMvc.perform(patch("/cart-items/{id}", 1)
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"@", "1.2"})
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void invalidIDType(String id) throws Exception {
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(5);

            mockMvc.perform(patch("/cart-items/{id}", id)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("올바르지 않은 타입입니다. 필요한 타입은 [Long]입니다. 현재 입력값: " + id));
        }

        @Test
        @DisplayName("상품 수량이 존재하지 않으면 400 상태를 반환한다.")
        void nullQuantity() throws Exception {
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(null);

            mockMvc.perform(patch("/cart-items/{id}", 1)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(
                            "요청 데이터이 유효하지 않습니다. 예외 메시지: [[필드명: quantity] 장바구니 상품 수량은 필수로 입력해야 합니다.]"));
        }

        @Test
        @DisplayName("유효한 요청이라면 장바구니 상품 수량을 수정한다.")
        void updateCartItemQuantity() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 10);
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(100);
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
            willDoNothing().given(cartItemService)
                    .updateQuantity(any(Member.class), anyLong(), any(CartItemQuantityUpdateRequest.class));

            mockMvc.perform(patch("/cart-items/{id}", 1)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("removeCartItems 메서드는 ")
    class RemoveCartItems {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            mockMvc.perform(delete("/cart-items/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"@", "1.2"})
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void invalidIDType(String id) throws Exception {
            mockMvc.perform(delete("/cart-items/{id}", id)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("올바르지 않은 타입입니다. 필요한 타입은 [Long]입니다. 현재 입력값: " + id));
        }

        @Test
        @DisplayName("유효한 요청이라면 장바구니 상품을 삭제한다.")
        void removeCartItems() throws Exception {
            willDoNothing().given(cartItemService).remove(any(Member.class), anyLong());

            mockMvc.perform(delete("/cart-items/{id}", 1)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                    )
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }
}
