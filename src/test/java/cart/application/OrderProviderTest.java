package cart.application;

import cart.application.order.OrderProvider;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.dto.OrderResponse;
import cart.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class OrderProviderTest {

    @InjectMocks
    private OrderProvider orderProvider;
    @Mock
    private OrderRepository orderRepository;

    @Test
    void 상품을_조회한다() {
        // given
        final Member member = new Member(1L, "a@a.com", "1234");
        final Product chicken = new Product(1L, "치킨", 10000, "imgUrl");
        final Product dessert = new Product(1L, "desert", 5000, "imgUrl");
        final OrderItem chickenOrderItem = new OrderItem(chicken, 1);
        final OrderItem desertOrderItem = new OrderItem(dessert, 1);
        final Coupon coupon = new Coupon(1L, "1000원 할인 쿠폰", "1000원이 할인 됩니다.", 1000, false);

        final Order order = new Order(List.of(chickenOrderItem, desertOrderItem), member, coupon, chicken.getPrice() + dessert.getPrice());
        given(orderRepository.findOrderByMemberId(anyLong())).willReturn(List.of(order));

        // when
        final List<OrderResponse> orderResponses = orderProvider.findOrderByMember(member);

        // then
        final OrderResponse result = orderResponses.get(0);
        assertAll(
                () -> assertThat(result.getOrderItems()).hasSize(2),
                () -> assertThat(result.getDate()).isNotNull(),
                () -> assertThat(result.getPrice()).isEqualTo(15000)
        );
    }
}
