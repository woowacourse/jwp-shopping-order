package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.OrderProductRepository;
import cart.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class OrderProductRepositoryTest {

    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void saveOrderProductsByOrderId() {
        Member member = new Member(1L, "a@a", "123");
        Order order = new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))), Coupon.EMPTY);
        Long orderId = orderRepository.save(order);

        assertDoesNotThrow(() -> orderProductRepository.save(orderId, order));
    }
}
