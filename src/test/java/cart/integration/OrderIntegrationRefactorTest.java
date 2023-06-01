package cart.integration;

import static cart.TestDataFixture.CART_ITEM_1;
import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.MEMBER_1_AUTH_HEADER;
import static cart.TestDataFixture.OBJECT_MAPPER;
import static cart.TestDataFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.order.Order;
import cart.service.response.OrderProductResponseDto;
import cart.service.response.OrderResponseDto;
import cart.service.response.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class OrderIntegrationRefactorTest extends IntegrationRefactorTest {

    @DisplayName("쿠폰을 사용하지 않은 주문내역을 조회한다.")
    @Test
    void findOrderWithoutCoupon() throws Exception {
        //given
        //상품이 존재한다.
        productRepository.insertProduct(PRODUCT_1);
        cartItemDao.save(CART_ITEM_1);
        //주문 내역에 값이 담겨있다.
        final Order order = Order.of(MEMBER_1, List.of(CART_ITEM_1));
        final Long savedOrderId = orderRepository.save(order).getId();

        //when
        final MvcResult result = mockMvc
                .perform(get("/order/" + savedOrderId)
                        .header("Authorization", MEMBER_1_AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String resultJsonString = result.getResponse().getContentAsString();
        final OrderResponseDto response = OBJECT_MAPPER.readValue(resultJsonString, OrderResponseDto.class);

        assertThat(response)
                .extracting(
                        OrderResponseDto::getTimestamp,
                        OrderResponseDto::getOriginPrice,
                        OrderResponseDto::getTotalPrice
                )
                .containsExactly(
                        order.getTimeStamp().toString(),
                        order.getOriginPrice().getValue(),
                        order.getTotalPrice().getValue()
                );

        assertThat(response.getOrderProducts())
                .extracting(OrderProductResponseDto::getQuantity)
                .containsExactly(CART_ITEM_1.getQuantity().getValue());

        assertThat(response.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponse)
                .extracting(
                        ProductResponse::getId,
                        ProductResponse::getName,
                        ProductResponse::getPrice,
                        ProductResponse::getImageUrl
                )
                .containsExactly(
                        tuple(
                                PRODUCT_1.getId(),
                                PRODUCT_1.getName(),
                                PRODUCT_1.getPrice().getValue(),
                                PRODUCT_1.getImageUrl()
                        )
                );
        System.out.println(resultJsonString);
    }
}
