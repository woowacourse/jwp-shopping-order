package cart.controller;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.coupon.DiscountRequest;
import cart.dto.coupon.MemberCouponRequest;
import cart.dto.order.OrderItemRequest;
import cart.dto.order.OrderItemsRequests;
import cart.dto.order.OrderProductRequest;
import cart.fixture.Fixture;
import cart.repository.MemberRepository;
import cart.ui.OrderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(value = OrderController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderApiControllerTest {

    String email = "a@a.com";
    String password = "1234";

    String authString = email + ":" + password;
    String encodedAuthString = new String(Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8)));

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 주문_추가() throws Exception {
        //given
        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));
        OrderItemsRequests request = new OrderItemsRequests(
                3000,
                List.of(new OrderItemRequest(
                        2L,
                        new OrderProductRequest(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"),
                        10,
                        List.of(new MemberCouponRequest(
                                1L,
                                "오픈 기념 쿠폰",
                                new DiscountRequest("rate", 10)
                        ))
                )));

        //when
        ResultActions result = mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Basic " + encodedAuthString)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("order-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        requestFields(
                                fieldWithPath("deliveryFee").description("배송비"),
                                fieldWithPath("orderItems").description("장바구니 상품 리스트"),
                                fieldWithPath("orderItems[].id").description("장바구니 상품 id"),
                                fieldWithPath("orderItems[].quantity").description("장바구니 상품 개수"),
                                fieldWithPath("orderItems[].product").description("장바구니 상품 정보"),
                                fieldWithPath("orderItems[].product.id").description("상품 id"),
                                fieldWithPath("orderItems[].product.name").description("상품 이름"),
                                fieldWithPath("orderItems[].product.price").description("상품 가격"),
                                fieldWithPath("orderItems[].product.imageUrl").description("상품 이미지 URL"),
                                fieldWithPath("orderItems[].coupons").description("장바구니 적용 쿠폰"),
                                fieldWithPath("orderItems[].coupons[].couponId").description("쿠폰 id"),
                                fieldWithPath("orderItems[].coupons[].name").description("쿠폰 이름"),
                                fieldWithPath("orderItems[].coupons[].discount").description("할인"),
                                fieldWithPath("orderItems[].coupons[].discount.type").description("할인 유형"),
                                fieldWithPath("orderItems[].coupons[].discount.amount").description("할인 금액")
                        )
                ));

    }

    @Test
    void 단일_주문_조회() throws Exception {
        //given
        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));
        given(orderService.findById(eq(2L), any(Member.class))).willReturn(new Order(2L, 1L, 3000, List.of(Fixture.주문_제품_치킨, Fixture.주문_제품_피자)));

        //when
        ResultActions result = mockMvc.perform(get("/orders/{id}", 2L)
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Basic " + encodedAuthString));
        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        ),
                        pathParameters(
                                parameterWithName("id").description("주문 id")
                        )
                ));
    }

    @Test
    void 모든_주문_조회() throws Exception {
        //given
        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));
        given(orderService.findAllByMemberId(eq(1L))).willReturn(
                List.of(
                        new Order(1L, 1L, 3000, List.of(Fixture.주문_제품_치킨, Fixture.주문_제품_피자)),
                        new Order(2L, 1L, 2000, List.of(Fixture.주문_제품_치킨))
                )
        );

        //when
        ResultActions result = mockMvc.perform(get("/orders")
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Basic " + encodedAuthString));
        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("order-find-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        )
                ));
    }
}
