package cart.ui;

import static cart.helper.RestDocsHelper.prettyDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.MockAuthProviderConfig;
import cart.application.CartItemService;
import cart.config.auth.AuthProvider;
import cart.dto.User;
import cart.dao.MemberDao;
import cart.domain.cart.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CartItemApiController.class)
@AutoConfigureRestDocs
@Import(MockAuthProviderConfig.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CartItemApiControllerTest {
    private final Member member = new Member(1L, "aaa@google.com", "1234", 1000);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartItemService cartItemService;

    @MockBean
    MemberDao memberDao;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthProvider authProvider;

    @BeforeEach
    void setUp() {
        given(authProvider.resolveUser(anyString()))
                .willReturn(new User(1L, "a@a.com"));
    }

    @Test
    void 회원의_모든_장바구니_품목을_조회한다() throws Exception {
        Product product = new Product(1L, "곰돌이", 10000, "http:localhost:8080");
        CartItem cartItem = new CartItem(1L, 3, product, member);
        CartItemResponse cartItemResponse = CartItemResponse.of(cartItem);
        given(cartItemService.findAllByMember(anyLong())).willReturn(List.of(cartItemResponse));
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(get("/cart-items")
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andDo(prettyDocument(
                        "cart-items/showCartItems"
                ))
                .andDo(print());
    }

    @Test
    void 회원의_장바구니에_품목을_등록한다() throws Exception {
        CartItemRequest cartItemRequest = new CartItemRequest(1L);
        given(cartItemService.add(anyLong(), any(CartItemRequest.class))).willReturn(1L);
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/cart-items")
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0")
                        .content(objectMapper.writeValueAsString(cartItemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(prettyDocument(
                        "cart-items/addCartItems"
                ))
                .andDo(print());
    }

    @Test
    void 장바구니에_품목을_등록할때_품목의_ID가_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        CartItemRequest cartItemRequest = new CartItemRequest(null);
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/cart-items")
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0")
                        .content(objectMapper.writeValueAsString(cartItemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.productId").value("상품 ID는 반드시 포함되어야 합니다."));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void 장바구니에_품목을_등록할때_품목의_ID가_0_또는_음수이면_400_상태코드가_반환된다(Long productId) throws Exception {
        CartItemRequest cartItemRequest = new CartItemRequest(productId);
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/cart-items")
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0")
                        .content(objectMapper.writeValueAsString(cartItemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.productId").value("상품 ID는 0 또는 음수가 될 수 없습니다."));
    }

    @Test
    void 회원의_장바구니_수량을_변경한다() throws Exception {
        CartItemQuantityUpdateRequest updateRequest = new CartItemQuantityUpdateRequest(10);
        willDoNothing().given(cartItemService)
                .updateQuantity(anyLong(), anyLong(), any(CartItemQuantityUpdateRequest.class));
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/cart-items/{id}", 1L)
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0")
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(prettyDocument(
                        "cart-items/updateCartItems",
                        pathParameters(
                                parameterWithName("id").description("장바구니 ID")
                        ),
                        requestFields(
                                fieldWithPath("quantity").description("상품 수량")
                        )
                ))
                .andDo(print());
    }

    @Test
    void 장바구니_수량을_변경할때_수량이_포함되지_않으면_400_상태코드가_반환된다() throws Exception {
        CartItemQuantityUpdateRequest updateRequest = new CartItemQuantityUpdateRequest(null);
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/cart-items/{id}", 1L)
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0")
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.quantity").value("수량은 반드시 포함되어야 합니다."));
    }

    @Test
    void 장바구니_수량을_변경할때_수량이_음수이면_400_상태코드가_반환된다() throws Exception {
        CartItemQuantityUpdateRequest updateRequest = new CartItemQuantityUpdateRequest(-1);
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/cart-items/{id}", 1L)
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0")
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.quantity").value("수량은 음수가 될 수 없습니다."));
    }

    @Test
    void 회원의_장바구니_품목을_삭제한다() throws Exception {
        willDoNothing().given(cartItemService)
                .remove(anyLong(), anyLong());
        given(memberDao.findByEmail(any())).willReturn(Optional.of(member));

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/cart-items/{id}", 1L)
                        .header("Authorization", "basic " + "YUBhLmNvbToxMjM0"))
                .andExpect(status().isOk())
                .andDo(prettyDocument(
                        "cart-items/deleteCartItems",
                        pathParameters(
                                parameterWithName("id").description("장바구니 ID")
                        )
                ))
                .andDo(print());
    }
}
