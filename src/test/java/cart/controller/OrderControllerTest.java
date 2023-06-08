package cart.controller;

import cart.BasicAuthorizationEncoder;
import cart.auth.BasicAuthorizationExtractor;
import cart.auth.dao.AuthDao;
import cart.auth.dto.AuthorizationDto;
import cart.controller.api.OrderController;
import cart.controller.dto.request.OrderRequest;
import cart.domain.Member;
import cart.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @BeforeEach
    void setUp() {
        final String email = "ditoo@wooteco.com";
        final String password = "ditoo1234";
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
            given(orderService.save(any(), any()))
                    .willReturn(1L);
            final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 33000);
            final String requestBody = objectMapper.writeValueAsString(orderRequest);

            // when
            mockMvc.perform(post("/orders")
                            .header("Authorization", authenticationHeader)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(print())
                    .andDo(document("orders/create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                    .andExpect(status().isCreated());
        }
    }
}
