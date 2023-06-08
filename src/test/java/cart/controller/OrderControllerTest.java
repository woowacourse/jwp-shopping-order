package cart.controller;

import cart.BasicAuthorizationEncoder;
import cart.auth.dao.AuthDao;
import cart.auth.dto.AuthorizationDto;
import cart.controller.api.OrderController;
import cart.controller.dto.request.OrderRequest;
import cart.exception.AuthorizationException;
import cart.exception.CartItemNotFoundException;
import cart.exception.PaymentAmountNotEqualException;
import cart.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private AuthDao authDao;

    private String authenticationHeader;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = "ditoo@wooteco.com";
        password = "ditoo1234";
        final AuthorizationDto authorizationDto = new AuthorizationDto(email, password);
        authenticationHeader = BasicAuthorizationEncoder.encode(authorizationDto.getEmail(), authorizationDto.getPassword());
    }

    @Nested
    @DisplayName("주문 요청 - POST /orders")
    class Create {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            given(orderService.save(any(), any())).willReturn(1L);
            final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 25_000);
            final String requestBody = objectMapper.writeValueAsString(orderRequest);

            // when, then
            mockMvc.perform(post("/orders")
                            .header("Authorization", authenticationHeader)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andDo(document("orders/create/success", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("실패 - 금액 불일치")
        void fail_wrong_payment_amount() throws Exception {
            // given
            final int wrongPaymentAmount = 23_000;
            final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), wrongPaymentAmount);
            final String requestBody = objectMapper.writeValueAsString(orderRequest);

            // when
            when(orderService.save(any(), any())).thenThrow(PaymentAmountNotEqualException.class);

            // then
            mockMvc.perform(post("/orders")
                            .header("Authorization", authenticationHeader)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andDo(document("orders/create/fail-wrongPrice", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("실패 - 유저 인증 실패")
        void fail_unauthorized() throws Exception {
            // given
            final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 25_000);
            final String requestBody = objectMapper.writeValueAsString(orderRequest);

            // when
            when(authDao.findIdByEmailAndPassword(email, password)).thenThrow(AuthorizationException.class);

            // then
            mockMvc.perform(post("/orders")
                            .header("Authorization", authenticationHeader)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andDo(document("orders/create/fail-authorization", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 장바구니 상품")
        void fail_cartItem_not_found() throws Exception {
            // given
            final Long wrongProductId = 77777L;
            final OrderRequest orderRequest = new OrderRequest(List.of(1L, wrongProductId), 25_000);
            final String requestBody = objectMapper.writeValueAsString(orderRequest);

            // when
            when(orderService.save(any(), any())).thenThrow(CartItemNotFoundException.class);

            // then
            mockMvc.perform(post("/orders")
                            .header("Authorization", authenticationHeader)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andDo(document("orders/create/fail-cartItemNotFound", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                    .andExpect(status().isNotFound());
        }
    }
}
