package cart.ui;

import static cart.ui.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.configuration.WebMvcConfig;
import cart.application.CartItemService;
import cart.configuration.resolver.MemberArgumentResolver;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(
        controllers = CartItemApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class)
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class CartItemApiControllerTest {

    @MockBean
    MemberArgumentResolver memberArgumentResolver;

    @MockBean
    CartItemService cartItemService;

    @Autowired
    CartItemApiController cartItemApiController;
    @Autowired
    RestDocumentationResultHandler restDocs;
    @Autowired
    ObjectMapper objectMapper;
    MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired final RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.standaloneSetup(cartItemApiController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setCustomArgumentResolvers(memberArgumentResolver)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }

    @Test
    void showCartItems() throws Exception {
        final Member member = new Member(1L, "a@a.com", "1234");
        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
        final Product product = new Product(1L, "A", 1000, "http://image.com");
        final CartItem cartItem = new CartItem(1L, 3, member, product);
        final CartItemResponse response = CartItemResponse.of(cartItem);
        given(cartItemService.findByMember(any(Member.class))).willReturn(List.of(response));

        mockMvc.perform(get("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                responseFields(
                                        fieldWithPath("[0].id").type(JsonFieldType.NUMBER).description("장바구니 상품 ID"),
                                        fieldWithPath("[0].quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량"),
                                        fieldWithPath("[0].product.id").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("[0].product.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("[0].product.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("[0].product.imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소")
                                )
                        )
                );
    }

    @Test
    void addCartItems() throws Exception {
        final Member member = new Member(1L, "a@a.com", "1234");
        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        final CartItemRequest request = new CartItemRequest(1L);
        given(cartItemService.add(any(Member.class), any(CartItemRequest.class))).willReturn(1L);

        mockMvc.perform(post("/cart-items")
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                requestFields(
                                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID").attributes(field("constraint", "양수"))
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("생성된 장바구니 상품 리소스 URL")
                                )
                        )
                );

    }

    @Test
    void updateCartItemQuantity() throws Exception {
        final Member member = new Member(1L, "a@a.com", "1234");
        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        final CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(3);
        willDoNothing().given(cartItemService).updateQuantity(any(Member.class), anyLong(), any(CartItemQuantityUpdateRequest.class));

        mockMvc.perform(patch("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                pathParameters(
                                        parameterWithName("id").description("장바구니 상품 ID")
                                ),
                                requestFields(
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량").attributes(field("constraint", "양수"))
                                )
                        )
                );

    }

    @Test
    void removeCartItems() throws Exception {
        final Member member = new Member(1L, "a@a.com", "1234");
        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);

        willDoNothing().given(cartItemService).remove(any(Member.class), anyLong());

        mockMvc.perform(delete("/cart-items/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab"))
                .andExpect(status().isNoContent())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                pathParameters(
                                        parameterWithName("id").description("장바구니 상품 ID")
                                )
                        )
                );
    }
}
