package cart.ui;

import static cart.utils.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.CartItemService;
import cart.domain.Member;
import cart.dto.cart.CartItemQuantityUpdateRequest;
import cart.dto.cart.CartItemRequest;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import cart.repository.dao.ProductDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(CartItemApiController.class)
@AutoConfigureRestDocs
class CartItemApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;
    @MockBean
    private ProductDao productDao;
    @MockBean
    private CartItemDao cartItemDao;
    @MockBean
    private ProductApiController productApiController;
    @MockBean
    private MemberDao memberDao;

    @Test
    void addCartItems() throws Exception {
        final String email = "test@email.com";
        final Member member = new Member(1L, email, "password");
        willReturn(member).given(memberDao).getMemberByEmail(email);
        willReturn(1L).given(cartItemService).add(member, new CartItemRequest(2L));

        this.mockMvc.perform(post("/cart-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\": 2}")
                        .header("Authorization", "Basic dGVzdEBlbWFpbC5jb206cGFzc3dvcmQ="))
                .andExpect(status().isCreated())
                .andDo(customDocument("add-cart-item",
                        requestHeaders(
                                headerWithName("Authorization").description("{email}:{password} - Base64 인코딩 값")
                        ),
                        requestFields(
                                fieldWithPath("productId").description("상품 id")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("추가된 장바구니 상품 resource 위치")
                        )
                ));
    }

    @Test
    void updateCartItemQuantity() throws Exception {
        final String email = "test@email.com";
        final Member member = new Member(1L, email, "password");
        willReturn(member).given(memberDao).getMemberByEmail(email);
        willDoNothing().given(cartItemService).updateQuantity(member, 2L, new CartItemQuantityUpdateRequest(10));

        this.mockMvc.perform(patch("/cart-items/{cartItemId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 10}")
                        .header("Authorization", "Basic dGVzdEBlbWFpbC5jb206cGFzc3dvcmQ="))
                .andExpect(status().isOk())
                .andDo(customDocument("update-cart-item-quantity",
                        requestHeaders(
                                headerWithName("Authorization").description("{email}:{password} - Base64 인코딩 값")
                        ),
                        pathParameters(
                                parameterWithName("cartItemId").description("장바구니 상품 id")
                        ),
                        requestFields(
                                fieldWithPath("quantity").description("상품 개수")
                        )
                ));
    }

    @Test
    void removeCartItems() throws Exception {
        final String email = "test@email.com";
        final Member member = new Member(1L, email, "password");
        willReturn(member).given(memberDao).getMemberByEmail(email);
        willDoNothing().given(cartItemService).remove(member, 2L);

        this.mockMvc.perform(delete("/cart-items/{cartItemId}", 2L)
                        .header("Authorization", "Basic dGVzdEBlbWFpbC5jb206cGFzc3dvcmQ="))
                .andExpect(status().isNoContent())
                .andDo(customDocument("delete-cart-item",
                        requestHeaders(
                                headerWithName("Authorization").description("{email}:{password} - Base64 인코딩 값")
                        ),
                        pathParameters(
                                parameterWithName("cartItemId").description("장바구니 상품 id")
                        )
                ));
    }
}
