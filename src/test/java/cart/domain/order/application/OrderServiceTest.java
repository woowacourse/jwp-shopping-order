package cart.domain.order.application;

import static cart.fixtures.CartItemFixtures.Ber_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.MemberFixtures.Dooly;
import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import cart.domain.cartitem.domain.CartItem;
import cart.domain.member.application.MemberService;
import cart.domain.member.application.dto.MemberCashChargeRequest;
import cart.domain.order.application.dto.OrderRequest;
import cart.domain.order.domain.dto.OrderCartItemDto;
import cart.domain.product.domain.Product;
import cart.global.config.AuthMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Nested
    @DisplayName("장바구니 상품 주문 시")
    class order {

        @Test
        @DisplayName("요청으로 온 주문 상품 이름과 사용자의 장바구니 정보가 다르면 예외가 발생한다.")
        void throws_when_not_match_request_order_cartItem_name() {
            // given
            AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
            OrderRequest request = Dooly_Order1.UPDATE_NAME_REQUEST();

            // when, then
            Assertions.assertThatThrownBy(() -> orderService.order(authMember, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("요청으로 온 주문 상품 가격과 사용자의 장바구니 정보가 다르면 예외가 발생한다.")
        void throws_when_not_match_request_order_cartItem_price() {
            // given
            AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
            OrderRequest request = Dooly_Order1.UPDATE_PRICE_REQUEST();

            // when, then
            Assertions.assertThatThrownBy(() -> orderService.order(authMember, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("요청으로 온 주문 상품 이름과 사용자의 장바구니 정보가 다르면 예외가 발생한다.")
        void throws_when_not_match_request_order_cartItem_imageUrl() {
            // given
            AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
            OrderRequest request = Dooly_Order1.UPDATE_IMAGE_URL_REQUEST();

            // when, then
            Assertions.assertThatThrownBy(() -> orderService.order(authMember, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.");
        }

        @Test
        @DisplayName("요청으로 온 장바구니 상품 아이디 중 하나라도 해당 사용자의 장바구니 상품이 아니면 예외가 발생한다.")
        void throws_when_not_match_request_order_cartItem_Id() {
            // given
            AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
            CartItem cartItem1 = Dooly_CartItem1.ENTITY();
            Product product1 = Dooly_CartItem1.PRODUCT;
            CartItem cartItem2 = Ber_CartItem1.ENTITY();
            Product product2 = Ber_CartItem1.PRODUCT;

            List<OrderCartItemDto> orderCartItemDtos = List.of(
                    new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                    new OrderCartItemDto(cartItem2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl())
            );
            OrderRequest request = new OrderRequest(orderCartItemDtos);

            // when, then
            Assertions.assertThatThrownBy(() -> orderService.order(authMember, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("장바구니 상품에 없는 상품입니다.");
        }

        @Test
        @DisplayName("총 주문 금액보다 사용자의 현재 잔액이 적다면 예외가 발생한다.")
        void throws_when_current_member_cash_less() {
            // given
            AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
            OrderRequest request = Dooly_Order1.REQUEST();

            // when, then
            Assertions.assertThatThrownBy(() -> orderService.order(authMember, request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사용자의 현재 금액이 주문 금액보다 적습니다.");
        }
    }

    @Test
    @DisplayName("성공하면 저장된 OrderId를 반환한다.")
    void success() {
        // given
        AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
        memberService.chargeCash(authMember, new MemberCashChargeRequest(100000));
        OrderRequest request = Dooly_Order1.REQUEST();

        // when
        Long orderId = orderService.order(authMember, request);

        // then
        assertThat(orderId).isNotNull();
    }
}
