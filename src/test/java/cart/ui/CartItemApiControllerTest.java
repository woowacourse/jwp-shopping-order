package cart.ui;

import static cart.fixtures.CartItemFixtures.바닐라_크림_콜드브루_ID_4_3개_17400원;
import static cart.fixtures.CartItemFixtures.아메리카노_ID_3_8개_36000원;
import static cart.fixtures.CartItemFixtures.유자_민트_티_ID_1_5개_29500원;
import static cart.fixtures.CartItemFixtures.자몽_허니_블랙티_ID_2_7개_39900원;
import static cart.utils.RestDocsHelper.customDocument;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.CartItemService;
import cart.repository.dao.CartItemDao;
import cart.repository.dao.MemberDao;
import cart.repository.dao.ProductDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.CartItemResponse;
import java.util.List;
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
    void showCartItems() throws Exception {
        final String email = "test@email.com";
        final Member member = new Member(1L, email, "password");
        willReturn(member).given(memberDao).getMemberByEmail(email);

        final List<CartItemResponse> cartItems = List.of(
                CartItemResponse.of(유자_민트_티_ID_1_5개_29500원(member)),
                CartItemResponse.of(자몽_허니_블랙티_ID_2_7개_39900원(member)),
                CartItemResponse.of(아메리카노_ID_3_8개_36000원(member)),
                CartItemResponse.of(바닐라_크림_콜드브루_ID_4_3개_17400원(member))
        );
        willReturn(cartItems).given(cartItemService).findByMember(member);

        this.mockMvc.perform(get("/cart-items")
                        .header("Authorization", "Basic dGVzdEBlbWFpbC5jb206cGFzc3dvcmQ="))
                .andExpect(status().isOk())
                .andDo(customDocument("show-cart-items",
                        requestHeaders(
                                headerWithName("Authorization").description("{email}:{password} - Base64 인코딩 값")
                        ),
                        responseFields(
                                beneathPath("[]"),
                                fieldWithPath("id").description("장바구니 상품 id"),
                                fieldWithPath("quantity").description("상품 개수"),
                                fieldWithPath("product.id").description("상품 id"),
                                fieldWithPath("product.name").description("상품 이름"),
                                fieldWithPath("product.price").description("상품 가격"),
                                fieldWithPath("product.imageUrl").description("상품 이미지")
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
