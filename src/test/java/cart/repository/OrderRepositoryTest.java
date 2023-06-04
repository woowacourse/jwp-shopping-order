package cart.repository;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.OrderRepository;
import cart.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("주문을 저장한다.")
    void saveOrder() {
        Member member = memberDao.findById(1L);
        Long 오션 = productRepository.save(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.save(new Product("바다", 100, "바다.jpg"));
        Long 카트_오션 = cartItemRepository.save(new CartItem(member, productRepository.findById(오션)));
        Long 카트_바다 = cartItemRepository.save(new CartItem(member, productRepository.findById(바다)));
        List<CartItem> cartItems = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        assertDoesNotThrow(() -> orderRepository.save(new Order(member, cartItems, Coupon.EMPTY)));
    }

    @Test
    @DisplayName("특정 사용자의 주문을 조회한다.")
    void findAllByMemberId() {
        Member member = memberDao.findById(1L);
        Long 오션 = productRepository.save(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.save(new Product("바다", 100, "바다.jpg"));
        CartItem cartItem1 = new CartItem(1L, 1, productRepository.findById(오션), member);
        CartItem cartItem2 = new CartItem(2L, 1, productRepository.findById(바다), member);
        Long 카트_오션 = cartItemRepository.save(cartItem1);
        Long 카트_바다 = cartItemRepository.save(cartItem2);
        List<CartItem> cartItems1 = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        Long 동해 = productRepository.save(new Product("동해", 1000, "동해.jpg"));
        Long 서해 = productRepository.save(new Product("서해", 100, "서해.jpg"));
        CartItem cartItem3 = new CartItem(1L, 1, productRepository.findById(동해), member);
        CartItem cartItem4 = new CartItem(2L, 1, productRepository.findById(서해), member);
        Long 카트_동해 = cartItemRepository.save(cartItem3);
        Long 카트_서해 = cartItemRepository.save(cartItem4);
        List<CartItem> cartItems2 = List.of(cartItemRepository.findById(카트_동해), cartItemRepository.findById(카트_서해));
        Order order1 = new Order(member, cartItems1, Coupon.EMPTY);
        Order order2 = new Order(member, cartItems2, Coupon.EMPTY);
        Long orderId1 = orderRepository.save(order1);
        Long orderId2 = orderRepository.save(order2);

        List<Order> orders = orderRepository.findAllByMemberId(member);

        assertAll(
                () -> assertThat(orders.get(0).getMember()).usingRecursiveComparison().isEqualTo(member),
                () -> assertThat(orders.get(0).getCartProducts().get(0).getProduct().getName()).isEqualTo("오션"),
                () -> assertThat(orders.get(0).getCartProducts().get(1).getProduct().getName()).isEqualTo("바다"),
                () -> assertThat(orders.get(0).calculatePrice()).isEqualTo(1100),
                () -> assertThat(orders.get(1).getMember()).usingRecursiveComparison().isEqualTo(member),
                () -> assertThat(orders.get(1).getCartProducts().get(0).getProduct().getName()).isEqualTo("동해"),
                () -> assertThat(orders.get(1).getCartProducts().get(1).getProduct().getName()).isEqualTo("서해"),
                () -> assertThat(orders.get(1).calculatePrice()).isEqualTo(1100)
        );
    }

    @Test
    @DisplayName("특정 사용자의 특정 주문을 조회한다.")
    void findByOrderId() {
        Member member = memberDao.findById(1L);
        Long 오션 = productRepository.save(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.save(new Product("바다", 100, "바다.jpg"));
        CartItem cartItem1 = new CartItem(1L, 1, productRepository.findById(오션), member);
        CartItem cartItem2 = new CartItem(2L, 1, productRepository.findById(바다), member);
        Long 카트_오션 = cartItemRepository.save(cartItem1);
        Long 카트_바다 = cartItemRepository.save(cartItem2);
        List<CartItem> cartItems1 = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        Order requestOrder = new Order(member, cartItems1, Coupon.EMPTY);
        Long orderId1 = orderRepository.save(requestOrder);

        Order order = orderRepository.findByIdAndMemberId(member, orderId1);

        assertAll(
                () -> assertThat(order.getMember()).usingRecursiveComparison().isEqualTo(member),
                () -> assertThat(order.getCartProducts().get(0).getProduct().getName()).isEqualTo("오션"),
                () -> assertThat(order.getCartProducts().get(1).getProduct().getName()).isEqualTo("바다"),
                () -> assertThat(order.calculatePrice()).isEqualTo(1100)
        );
    }
    @Test
    @DisplayName("특정 사용자의 특정 주문을 취소한다.")
    void deleteOrder() {
        Member member = memberDao.findById(1L);
        Long 오션 = productRepository.save(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.save(new Product("바다", 100, "바다.jpg"));
        CartItem cartItem1 = new CartItem(1L, 1, productRepository.findById(오션), member);
        CartItem cartItem2 = new CartItem(2L, 1, productRepository.findById(바다), member);
        Long 카트_오션 = cartItemRepository.save(cartItem1);
        Long 카트_바다 = cartItemRepository.save(cartItem2);
        List<CartItem> cartItems1 = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        Order requestOrder = new Order(member, cartItems1, Coupon.EMPTY);
        Long orderId1 = orderRepository.save(requestOrder);

        assertDoesNotThrow(()->orderRepository.deleteById(requestOrder.getId()));
    }

    @Test
    @DisplayName("특정 사용자의 주문을 확정한다.")
    void confirmOrder() {
        Member member = memberDao.findById(1L);
        Long 오션 = productRepository.save(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.save(new Product("바다", 100, "바다.jpg"));
        CartItem cartItem1 = new CartItem(1L, 1, productRepository.findById(오션), member);
        CartItem cartItem2 = new CartItem(2L, 1, productRepository.findById(바다), member);
        Long 카트_오션 = cartItemRepository.save(cartItem1);
        Long 카트_바다 = cartItemRepository.save(cartItem2);
        List<CartItem> cartItems1 = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        Order requestOrder = new Order(member, cartItems1, Coupon.EMPTY);
        Long orderId1 = orderRepository.save(requestOrder);

        orderRepository.confirmById(orderId1,member);
        assertAll(
                ()->assertThat(orderRepository.findByIdAndMemberId(member,orderId1).getMember().getEmail()).isEqualTo(member.getEmail()),
                ()->assertThat(orderRepository.findByIdAndMemberId(member,orderId1).calculatePrice()).isEqualTo(requestOrder.calculatePrice())

        );
    }
}
