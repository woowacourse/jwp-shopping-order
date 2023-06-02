package cart.repository;

import static cart.TestDataFixture.MEMBER_1;
import static cart.TestDataFixture.PRODUCT_1;
import static cart.TestDataFixture.PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThat;

import cart.RepositoryTest;
import cart.dao.CartItemDao;
import cart.domain.order.Order;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Order를 저장하는 기능 테스트")
    void saveOrder() {
        final Product savedProduct1 = productRepository.insertProduct(PRODUCT_1);
        final Product savedProduct2 = productRepository.insertProduct(PRODUCT_2);
        final CartItem savedCartItem1 = cartItemDao.save(new CartItem(MEMBER_1, savedProduct1));
        final CartItem savedCartItem2 = cartItemDao.save(new CartItem(MEMBER_1, savedProduct2));
        final List<CartItem> cartItems = List.of(savedCartItem1, savedCartItem2);
        final Order order = Order.of(MEMBER_1, cartItems);

        final Order orderAfterSave = orderRepository.save(order, cartItems);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMemberId)
                .contains(order.getTimeStamp(), MEMBER_1.getId());
        assertThat(orderAfterSave.getOrderProducts())
                .usingRecursiveComparison()
                .isEqualTo(cartItems);
    }

    @Test
    @DisplayName("Order를 조회하는 기능 테스트")
    void findById() {
        final Product savedProduct1 = productRepository.insertProduct(PRODUCT_1);
        final Product savedProduct2 = productRepository.insertProduct(PRODUCT_2);
        final CartItem savedCartItem1 = cartItemDao.save(new CartItem(MEMBER_1, savedProduct1));
        final CartItem savedCartItem2 = cartItemDao.save(new CartItem(MEMBER_1, savedProduct2));
        final List<CartItem> cartItems = List.of(savedCartItem1, savedCartItem2);
        final Order order = Order.of(MEMBER_1, cartItems);
        final Long orderId = orderRepository.save(order, cartItems).getId();

        final Order orderAfterSave = orderRepository.findById(orderId);

        assertThat(orderAfterSave)
                .extracting(Order::getTimeStamp, Order::getMemberId)
                .contains(order.getTimeStamp(), MEMBER_1.getId());
        assertThat(orderAfterSave.getOrderProducts())
                .usingRecursiveComparison()
                .isEqualTo(cartItems);
    }
}
