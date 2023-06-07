package cart.ui;

import cart.dto.request.OrderRequestDto;
import cart.dto.response.OrderDetail;
import cart.dto.response.OrderInfo;
import cart.dto.response.OrderResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * final OrderRequestDto orderRequestDto = new OrderRequestDto(
 * List.of(1L, 2L),
 * 103000,
 * 1L
 * );
 * <p>
 * 메서드에서 사용되고 있는 유저는 cartItem 1,2 를 가지고 있다.
 * cartItem 1L : 10000 * 2
 * cartItem 2L : 20000 * 4
 * deliveryFee : 3000
 * couponDiscount : 2000 , Id : 1L
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("존재하지 않는 카트 아이템에 대한 요청은 실패한다.")
    @Test
    void order_invalid_cartItemNotExist() throws Exception {
        //given
        final OrderRequestDto orderRequestDto = new OrderRequestDto(
                List.of(1L, 3L),
                103000,
                1L
        );

        //when, then
        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("order-cartItemNotExist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @DisplayName("자신이 가지고 있지 않은 쿠폰과 함께 요청을 보내면 실패한다.")
    @Test
    void order_invalid_couponDoesNotFound() throws Exception {
        //given
        final OrderRequestDto orderRequestDto = new OrderRequestDto(
                List.of(1L, 3L),
                103000,
                3L
        );

        //when, then
        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("order-couponDoesNotFound",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @DisplayName("계산 예상 금액과 다르면 예외가 발생한다.")
    @Test
    void order_invalid_unMatchCalculate() throws Exception {
        //given
        final OrderRequestDto orderRequestDto = new OrderRequestDto(
                List.of(1L, 2L),
                100000,
                1L
        );

        //when, then
        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(document("order-unMatchCalculate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @DisplayName("쿠폰 적용 정상 요청 테스트")
    @Test
    void order_valid_withCoupon() throws Exception {
        //given
        final OrderRequestDto orderRequestDto = new OrderRequestDto(
                List.of(1L, 2L),
                101000,
                1L
        );

        //when, then
        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("order-withCoupon",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @DisplayName("쿠폰 미적용 정상 요청 테스트")
    @Test
    void order_valid_withoutCoupon() throws Exception {
        //given
        final WithoutCouponRequestDto orderRequestDto = new WithoutCouponRequestDto(
                List.of(1L, 2L),
                103000
        );

        //when, then
        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andDo(document("order-withoutCoupon",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

    @DisplayName("주문 목록 출력 테스트")
    @Test
    void orderList() throws Exception {
        //given
        final WithoutCouponRequestDto orderRequestDto = new WithoutCouponRequestDto(
                List.of(1L),
                23000
        );

        final OrderRequestDto couponOrderRequestDto = new OrderRequestDto(
                List.of(2L),
                81000,
                1L
        );

        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk());

        mockMvc.perform(
                        post("/orders")
                                .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(couponOrderRequestDto)))
                .andExpect(status().isOk());


        //when
        final List<OrderInfo> orderInfos = objectMapper.readValue(mockMvc.perform(
                                get("/orders")
                                        .header(HttpHeaders.AUTHORIZATION, basicHeader()))
                        .andExpect(status().isOk())
                        .andDo(document("orderList",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())))
                        .andReturn().getResponse().getContentAsString(),
                new TypeReference<List<OrderInfo>>() {
                }
        );

        //then
        assertAll(
                () -> assertThat(orderInfos).hasSize(2),
                () -> assertThat(orderInfos.get(0).getOrderItems()).hasSize(1),
                () -> assertThat(orderInfos.get(1).getOrderItems()).hasSize(1)
        );


    }

    @DisplayName("주문 상세 테스트")
    @Test
    void orderDetail() throws Exception {
        //given
        final OrderRequestDto couponOrderRequestDto = new OrderRequestDto(
                List.of(2L),
                81000,
                1L
        );

        final long orderId = objectMapper.readValue(mockMvc.perform(
                                post("/orders")
                                        .header(HttpHeaders.AUTHORIZATION, basicHeader())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(couponOrderRequestDto)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                OrderResponseDto.class).getOrderId();

        //when, then
        final OrderDetail orderDetail = objectMapper.readValue(mockMvc.perform(
                                get("/orders/" + orderId)
                                        .header(HttpHeaders.AUTHORIZATION, basicHeader()))
                        .andExpect(status().isOk())
                        .andDo(document("orderDetail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())))
                        .andReturn().getResponse().getContentAsString(),
                OrderDetail.class);

        assertThat(orderDetail.getTotalPrice()).isEqualTo(81000);
        assertThat(orderDetail.getOrder().getOrderItems()).hasSize(1);
    }

    private String basicHeader() {
        final String email = "a@a.com";
        final String password = "1234";
        return "Basic " + Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }

    class WithoutCouponRequestDto {
        private List<Long> cartItemIds;
        private int totalPrice;

        public WithoutCouponRequestDto() {
        }

        public WithoutCouponRequestDto(final List<Long> cartItemIds, final int totalPrice) {
            this.cartItemIds = cartItemIds;
            this.totalPrice = totalPrice;
        }

        public List<Long> getCartItemIds() {
            return cartItemIds;
        }

        public int getTotalPrice() {
            return totalPrice;
        }
    }
}
