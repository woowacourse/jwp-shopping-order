package cart.integration;

import static cart.TestDataFixture.DISCOUNT_5000_CONSTANT;
import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.MEMBER_1_AUTH_HEADER;
import static cart.TestDataFixture.OBJECT_MAPPER;
import static cart.TestDataFixture.PRODUCT_1;
import static cart.TestDataFixture.objectToJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import cart.domain.vo.Price;
import cart.service.request.OrderRequestDto;
import cart.service.response.OrderResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class OrderIntegrationRefactorTest extends IntegrationRefactorTest {

    @DisplayName("쿠폰을 사용하지 않은 주문내역을 조회한다.")
    @Test
    void findOrderWithoutCoupon() throws Exception {
        //given
        final Product savedProduct = productRepository.insertProduct(PRODUCT_1);
        final CartItem savedCartItem = cartItemDao.save(new CartItem(3, MEMBER_1, savedProduct));
        final List<CartItem> cartItems = List.of(savedCartItem);
        final Order savedOrder = orderRepository.save(Order.of(MEMBER_1, cartItems), cartItems);

        //when
        final MvcResult result = mockMvc
                .perform(get("/orders/" + savedOrder.getId())
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
        final Product savedProduct = productRepository.insertProduct(PRODUCT_1);
        final CartItem savedCartItem = cartItemDao.save(new CartItem(3, MEMBER_1, savedProduct));
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_5000_CONSTANT);
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(
                new MemberCoupon(savedCoupon, MEMBER_1.getId()));
        final List<CartItem> cartItems = List.of(savedCartItem);
        final Order savedOrder = orderRepository.save(Order.of(MEMBER_1, cartItems, savedMemberCoupon), cartItems);

        //when
        final MvcResult result = mockMvc
                .perform(get("/orders/" + savedOrder.getId())
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
        final Product savedProduct = productRepository.insertProduct(PRODUCT_1);
        final CartItem savedCartItem = cartItemDao.save(new CartItem(3, MEMBER_1, savedProduct));
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_5000_CONSTANT);
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(
                new MemberCoupon(savedCoupon, MEMBER_1.getId()));
        final List<CartItem> cartItems = List.of(savedCartItem);
        final Order savedOrder = orderRepository.save(Order.of(MEMBER_1, cartItems, savedMemberCoupon), cartItems);

        //when
        final MvcResult result = mockMvc
                .perform(get("/orders")
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

    @DisplayName("사용자가 쿠폰없이 주문하는 기능 테스트")
    @Test
    void orderWithoutCoupon() throws Exception {
        //given
        final Product savedProduct = productRepository.insertProduct(PRODUCT_1);
        final CartItem savedCartItem = cartItemDao.save(new CartItem(3, MEMBER_1, savedProduct));
        final OrderRequestDto request = new OrderRequestDto(List.of(savedCartItem.getId()), null);

        //when
        final String location = mockMvc
                .perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", MEMBER_1_AUTH_HEADER)
                        .content(objectToJsonString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        //then
        final Long orderId = Long.parseLong(location.split("/orders/")[1]);
        final Order order = orderRepository.findById(orderId);

        final Price originPrice = savedCartItem.getProduct().calculatePrice(savedCartItem.getQuantity());

        assertThat(order)
                .extracting(Order::getOriginPrice)
                .isEqualTo(originPrice);
    }

    @DisplayName("사용자가 쿠폰적용하고 주문하는 기능 테스트")
    @Test
    void orderWithCoupon() throws Exception {
        //given
        final Product savedProduct = productRepository.insertProduct(PRODUCT_1);
        final CartItem savedCartItem = cartItemDao.save(new CartItem(3, MEMBER_1, savedProduct));
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_5000_CONSTANT);
        final MemberCoupon savedMemberCoupon = memberCouponRepository
                .insert(new MemberCoupon(savedCoupon, MEMBER_1.getId()));

        final OrderRequestDto request = new OrderRequestDto(List.of(savedCartItem.getId()), savedMemberCoupon.getId());

        //when
        final String location = mockMvc
                .perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", MEMBER_1_AUTH_HEADER)
                        .content(objectToJsonString(request))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        //then
        final Long orderId = Long.parseLong(location.split("/orders/")[1]);
        final Order order = orderRepository.findById(orderId);

        final Price originPrice = savedCartItem.getProduct().calculatePrice(savedCartItem.getQuantity());
        final Price discountPrice = new Price(DISCOUNT_5000_CONSTANT.getValue());
        final Price totalPrice = originPrice.subtract(discountPrice);

        assertThat(order)
                .extracting(Order::getOriginPrice, Order::getDiscountPrice, Order::getTotalPrice)
                .containsExactly(originPrice, discountPrice, totalPrice);
    }
}
