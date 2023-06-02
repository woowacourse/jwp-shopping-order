package cart.repository;

import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.PRODUCT_1;
import static cart.TestDataFixture.PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThat;

import cart.RepositoryTest;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.CartItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Order를 저장하는 기능 테스트")
    void saveOrder() {
        final CartItem cartItem1 = new CartItem(MEMBER_1, PRODUCT_1);
        final CartItem cartItem2 = new CartItem(MEMBER_1, PRODUCT_2);
        final List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        final Order order = Order.of(MEMBER_1, cartItems);

        final Order orderAfterSave = orderRepository.save(order, cartItems);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMemberId)
                .contains(order.getTimeStamp(), MEMBER_1.getId());
        assertThat(orderAfterSave.getOrderProducts())
                .extracting(OrderProduct::getProduct)
                .contains(cartItem1.getProduct(), cartItem2.getProduct());
    }

    @Test
    @DisplayName("Order를 조회하는 기능 테스트")
    void findById() {
        final CartItem cartItem1 = new CartItem(MEMBER_1, PRODUCT_1);
        final CartItem cartItem2 = new CartItem(MEMBER_1, PRODUCT_2);
        final List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        final Order order = Order.of(MEMBER_1, cartItems);

        final Long orderId = orderRepository.save(order, cartItems).getId();

        final Order orderAfterSave = orderRepository.findById(orderId);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMemberId)
                .contains(order.getTimeStamp(), MEMBER_1.getId());
        assertThat(orderAfterSave.getOrderProducts())
                .extracting(OrderProduct::getProduct)
                .contains(cartItem1.getProduct(), cartItem2.getProduct());
    }
}
