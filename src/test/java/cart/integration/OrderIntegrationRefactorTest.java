package cart.integration;

import static cart.TestDataFixture.CART_ITEM_1;
import static cart.TestDataFixture.DISCOUNT_5000_CONSTANT;
import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.MEMBER_1_AUTH_HEADER;
import static cart.TestDataFixture.OBJECT_MAPPER;
import static cart.TestDataFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.service.response.OrderResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
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
        final Order savedOrder = orderRepository.save(order);

        //when
        final MvcResult result = mockMvc
                .perform(get("/order/" + savedOrder.getId())
                        .header("Authorization", MEMBER_1_AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String resultJsonString = result.getResponse().getContentAsString();
        final OrderResponseDto response = OBJECT_MAPPER.readValue(resultJsonString, OrderResponseDto.class);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(OrderResponseDto.from(savedOrder));
    }

    @DisplayName("쿠폰을 사용한 주문내역을 조회한다.")
    @Test
    void findOrderWithCoupon() throws Exception {
        //given
        //상품이 존재한다.
        productRepository.insertProduct(PRODUCT_1);
        cartItemDao.save(CART_ITEM_1);
        final Coupon coupon = couponRepository.insert(DISCOUNT_5000_CONSTANT);
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);
        final Order order = Order.of(MEMBER_1, List.of(CART_ITEM_1), savedMemberCoupon);
        final Order savedOrder = orderRepository.save(order);

        //when
        final MvcResult result = mockMvc
                .perform(get("/order/" + savedOrder.getId())
                        .header("Authorization", MEMBER_1_AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String resultJsonString = result.getResponse().getContentAsString();
        final OrderResponseDto response = OBJECT_MAPPER.readValue(resultJsonString, OrderResponseDto.class);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(OrderResponseDto.from(savedOrder));
    }

    @DisplayName("전체 주문내역을 조회한다.")
    @Test
    void findOrders() throws Exception {
        //given
        //상품이 존재한다.
        productRepository.insertProduct(PRODUCT_1);
        cartItemDao.save(CART_ITEM_1);
        final Coupon coupon = couponRepository.insert(DISCOUNT_5000_CONSTANT);
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);
        final Order order = Order.of(MEMBER_1, List.of(CART_ITEM_1), savedMemberCoupon);
        final Order savedOrder = orderRepository.save(order);

        //when
        final MvcResult result = mockMvc
                .perform(get("/order")
                        .header("Authorization", MEMBER_1_AUTH_HEADER))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        final String resultJsonString = result.getResponse().getContentAsString();
        final List<OrderResponseDto> response = OBJECT_MAPPER.readValue(resultJsonString, new TypeReference<>() {
        });

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(List.of(OrderResponseDto.from(savedOrder)));
    }
}
