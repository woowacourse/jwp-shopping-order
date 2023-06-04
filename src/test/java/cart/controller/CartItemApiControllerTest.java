package cart.controller;

import cart.application.CartItemService;
import cart.domain.member.Member;
import cart.dto.cartItem.CartItemRequest;
import cart.fixture.Fixture;
import cart.repository.MemberRepository;
import cart.ui.CartItemApiController;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(value = CartItemApiController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CartItemApiControllerTest {

    String email = "a@a.com";
    String password = "1234";

    String authString = email + ":" + password;
    String encodedAuthString = new String(Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8)));

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartItemService cartItemService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 유저_장바구니_조회() throws Exception {
        //given
        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));
        given(cartItemService.findByMember(any(Member.class)))
                .willReturn(List.of(Fixture.장바구니_치킨, Fixture.장바구니_피자));

        //when
        ResultActions result = mockMvc.perform(get("/cart-items")
                .header("Authorization", "Basic " + encodedAuthString));

        //then
        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("cart-item-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Basic Auth")
                        )
                ));
    }

    @Test
    void 장바구니_추가() throws Exception {
        //given
        given(memberRepository.getMemberByEmail(email)).willReturn(new Member(1L, email, password));
        given(cartItemService.add(any(Member.class), any(CartItemRequest.class))).willReturn(3L);
        CartItemRequest cartItemRequest = new CartItemRequest(1L);

        //when
        ResultActions result = mockMvc.perform(post("/cart-items")
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Basic " + encodedAuthString)
                .content(objectMapper.writeValueAsString(cartItemRequest)));

        //then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("3")))
                .andDo(print())
                .andDo(document("cart-item-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productId").description("제품 id")
                        )
                ));
    }
}
