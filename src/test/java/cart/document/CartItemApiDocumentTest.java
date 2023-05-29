package cart.document;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static cart.fixtures.MemberFixtures.Dooly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.application.CartItemService;
import cart.application.MemberService;
import cart.config.AuthMemberInterceptor;
import cart.config.MemberArgumentResolver;
import cart.config.WebMvcConfig;
import cart.dao.MemberDao;
import cart.dto.AuthMember;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.fixtures.ProductFixtures;
import cart.ui.CartItemApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(CartItemApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemApiDocumentTest {

    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private WebMvcConfig webMvcConfig;

    @MockBean
    private MemberDao memberDao;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(new CartItemApiController(cartItemService))
                .addInterceptors(new AuthMemberInterceptor(memberService))
                .setCustomArgumentResolvers(new MemberArgumentResolver())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void 특정_유저의_장바구니_목록_조회_문서화() throws Exception {
        // given
        given(memberDao.getMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY);
        given(cartItemService.findByMember(any(AuthMember.class)))
                .willReturn(List.of(Dooly_CartItem1.RESPONSE, Dooly_CartItem2.RESPONSE));
        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(get("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("cart-items/showCartItems",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                                        fieldWithPath("[].quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 개수"),
                                        fieldWithPath("[].product.id").type(JsonFieldType.NUMBER).description("장바구니 상품 아이디"),
                                        fieldWithPath("[].product.name").type(JsonFieldType.STRING).description("장바구니 상품 이름"),
                                        fieldWithPath("[].product.price").type(JsonFieldType.NUMBER).description("장바구니 상품 가격"),
                                        fieldWithPath("[].product.imageUrl").type(JsonFieldType.STRING).description("장바구니 상품 이미지 URL")
                                )
                        )
                );
    }

    @Test
    void 특정_유저의_장바구니_상품_추가_문서화() throws Exception {
        // given
        given(memberDao.getMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY);
        final CartItemRequest request = new CartItemRequest(ProductFixtures.CHICKEN.ID, 2);
        given(cartItemService.add(any(AuthMember.class), any(CartItemRequest.class)))
                .willReturn(Dooly_CartItem1.ID);
        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(post("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/cart-items/" + Dooly_CartItem1.ID))
                .andDo(document("cart-items/addCartItems",
                                preprocessRequest(prettyPrint()),
                                requestFields(
                                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("장바구니에 추가할 상품 아이디"),
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("장바구니에 추가할 상품 수량")
                                )
                        )
                );
    }

    @Test
    void 특정_유저의_장바구니_상품_수정_문서화() throws Exception {
        // given
        given(memberDao.getMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY);
        final CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(10);
        willDoNothing().given(cartItemService).updateQuantity(any(AuthMember.class), eq(Dooly_CartItem1.ID), eq(request));
        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(patch("/cart-items/{id}", Dooly_CartItem1.ID)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("cart-items/updateCartItemQuantity",
                                preprocessRequest(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("수정할 장바구니 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수정할 상품의 수량")
                                )
                        )
                );
    }

    @Test
    void 특정_유저의_장바구니_상품_삭제_문서화() throws Exception {
        // given
        given(memberDao.getMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY);
        willDoNothing().given(cartItemService).remove(any(AuthMember.class), eq(Dooly_CartItem1.ID));
        final String encodeAuthInfo = Base64Utils.encodeToString((Dooly.EMAIL + ":" + Dooly.PASSWORD).getBytes());

        // when, then
        mockMvc.perform(delete("/cart-items/{id}", Dooly_CartItem1.ID)
                        .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + encodeAuthInfo))
                .andExpect(status().isNoContent())
                .andDo(document("cart-items/removeCartItems",
                                pathParameters(
                                        parameterWithName("id").description("삭제할 장바구니 아이디")
                                )
                        )
                );
    }
}
