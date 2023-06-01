package cart.repository;

import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CartItemRepository;
import cart.domain.repository.OrderProductRepository;
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
    private OrderProductRepository orderProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("주문을 저장한다.")
    void saveOrder() {
        Member member = memberDao.getMemberById(1L);
        Long 오션 = productRepository.createProduct(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.createProduct(new Product("바다", 100, "바다.jpg"));
        Long 카트_오션 = cartItemRepository.save(new CartItem(member, productRepository.getProductById(오션)));
        Long 카트_바다 = cartItemRepository.save(new CartItem(member, productRepository.getProductById(바다)));
        List<CartItem> cartItems = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        assertDoesNotThrow(() -> orderRepository.saveOrder(new Order(member, cartItems, Coupon.empty())));
    }

    @Test
    @DisplayName("특정 사용자의 주문을 조회한다.")
    void findAllOrder() {
        Member member = memberDao.getMemberById(1L);
        Long 오션 = productRepository.createProduct(new Product("오션", 1000, "ocean.jpg"));
        Long 바다 = productRepository.createProduct(new Product("바다", 100, "바다.jpg"));
        CartItem cartItem1 = new CartItem(1L, 1, productRepository.getProductById(오션), member);
        CartItem cartItem2 = new CartItem(2L, 1, productRepository.getProductById(바다), member);
        Long 카트_오션 = cartItemRepository.save(cartItem1);
        Long 카트_바다 = cartItemRepository.save(cartItem2);
        List<CartItem> cartItems1 = List.of(cartItemRepository.findById(카트_오션), cartItemRepository.findById(카트_바다));

        Long 동해 = productRepository.createProduct(new Product("동해", 1000, "동해.jpg"));
        Long 서해 = productRepository.createProduct(new Product("서해", 100, "서해.jpg"));
        CartItem cartItem3 = new CartItem(1L, 1, productRepository.getProductById(동해), member);
        CartItem cartItem4 = new CartItem(2L, 1, productRepository.getProductById(서해), member);
        Long 카트_동해 = cartItemRepository.save(cartItem3);
        Long 카트_서해 = cartItemRepository.save(cartItem4);
        List<CartItem> cartItems2 = List.of(cartItemRepository.findById(카트_동해), cartItemRepository.findById(카트_서해));
        Order order1 = new Order(member, cartItems1, Coupon.empty());
        Order order2 = new Order(member, cartItems2, Coupon.empty());
        Long orderId1 = orderRepository.saveOrder(order1);
        Long orderId2 = orderRepository.saveOrder(order2);
        orderProductRepository.saveOrderProductsByOrderId(orderId1,order1);
        orderProductRepository.saveOrderProductsByOrderId(orderId2,order2);

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
}