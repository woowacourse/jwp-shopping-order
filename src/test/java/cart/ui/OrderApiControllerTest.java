package cart.ui;

import cart.Fixture;
import cart.WebMvcConfig;
import cart.application.OrderService;
import cart.domain.Member;
import cart.dto.OrderListResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static cart.ui.RestDocsConfiguration.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = OrderApiController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class)
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class OrderApiControllerTest {

    @MockBean
    MemberArgumentResolver memberArgumentResolver;
    @MockBean
    OrderService orderService;

    @Autowired
    OrderApiController orderApiController;
    @Autowired
    RestDocumentationResultHandler restDocs;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired final RestDocumentationContextProvider provider) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(orderApiController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setCustomArgumentResolvers(memberArgumentResolver)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();

        given(memberArgumentResolver.supportsParameter(any())).willReturn(true);
        given(memberArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(Fixture.memberA);
    }

    @Test
    void addOrder() throws Exception {
        final OrderRequest request = new OrderRequest(1000, List.of(1L, 2L, 3L));
        given(orderService.add(any(Member.class), any(OrderRequest.class))).willReturn(1L);

        mockMvc.perform(post("/orders")
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
                                        fieldWithPath("usedPoints").type(JsonFieldType.NUMBER).description("소모할 포인트").attributes(field("constraint", "양수")),
                                        fieldWithPath("cartItemIds").type(JsonFieldType.ARRAY).description("장바구니 상품 ID 목록").attributes(field("constraint", "1개 이상의 양수"))
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("생성된 장바구니 상품 리소스 URL")
                                )
                        )
                );
    }

    @Test
    void showOrderById() throws Exception {
        final OrderResponse response = OrderResponse.of(Fixture.order1);
        given(orderService.findById(any(Member.class), anyLong())).willReturn(response);

        mockMvc.perform(get("/orders/{id}", Fixture.order1.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                pathParameters(
                                        parameterWithName("id").description("주문 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 ID"),
                                        fieldWithPath("orderItems.[0].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("orderItems.[0].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("orderItems.[0].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("orderItems.[0].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소"),
                                        fieldWithPath("orderItems.[0].quantity").type(JsonFieldType.NUMBER).description("장바구니 상품 수량"),
                                        fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("주문 총 금액"),
                                        fieldWithPath("payPrice").type(JsonFieldType.NUMBER).description("실 결제 금액"),
                                        fieldWithPath("earnedPoints").type(JsonFieldType.NUMBER).description("포인트 적립 금액"),
                                        fieldWithPath("usedPoints").type(JsonFieldType.NUMBER).description("포인트 사용 금액"),
                                        fieldWithPath("orderDate").type(JsonFieldType.STRING).description("주문 일자")
                                )
                        )
                );
    }

    @Test
    void showOrdersFirst() throws Exception {
        final OrderListResponse response = OrderListResponse.of(List.of(Fixture.order2, Fixture.order1));
        given(orderService.findPageByIndex(any(Member.class), anyLong())).willReturn(response);

        mockMvc.perform(get("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab"))
                .andExpect(status().isOk());
    }

    @Test
    void showOrders() throws Exception {
        final OrderListResponse response = OrderListResponse.of(List.of(Fixture.order2, Fixture.order1));
        given(orderService.findPageByIndex(any(Member.class), anyLong())).willReturn(response);

        mockMvc.perform(get("/orders?last-id={idx}", 20L)
                        .header(HttpHeaders.AUTHORIZATION, "Basic ababababaababab"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Authorization").description("사용자 Basic 인증 정보").attributes(field("constraint", "Basic 형식 토큰"))
                                ),
                                requestParameters(
                                        parameterWithName("last-id").description("마지막으로 조회한 주문 ID, 첫 요청일시 포함하지 않음")
                                ),
                                responseFields(
                                        fieldWithPath("orders.[0].id").type(JsonFieldType.NUMBER).description("주문 ID"),
                                        fieldWithPath("orders.[0].orderItems.[0].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("orders.[0].orderItems.[0].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("orders.[0].orderItems.[0].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("orders.[0].orderItems.[0].imageUrl").type(JsonFieldType.STRING).description("상품 이미지 주소"),
                                        fieldWithPath("orders.[0].orderItems.[0].quantity").type(JsonFieldType.NUMBER).description("상품 주문 수량"),
                                        fieldWithPath("orders.[0].totalPrice").type(JsonFieldType.NUMBER).description("주문 총 금액"),
                                        fieldWithPath("orders.[0].payPrice").type(JsonFieldType.NUMBER).description("실 결제 금액"),
                                        fieldWithPath("orders.[0].earnedPoints").type(JsonFieldType.NUMBER).description("포인트 적립 금액"),
                                        fieldWithPath("orders.[0].usedPoints").type(JsonFieldType.NUMBER).description("포인트 사용 금액"),
                                        fieldWithPath("orders.[0].orderDate").type(JsonFieldType.STRING).description("주문 일자")
                                )
                        )
                );
    }
}
