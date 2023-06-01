package cart.service;

import static cart.TestDataFixture.MEMBER_3;
import static cart.TestDataFixture.PRODUCT_1;
import static cart.TestDataFixture.PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.dao.CartItemDao;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import cart.service.request.OrderRequestDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
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
    private OrderRepository orderRepository;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("쿠폰없이 상품을 주문하는 기능 테스트")
    @Test
    void orderProduct() {
        //given
        final Product savedProduct1 = productRepository.insertProduct(PRODUCT_1);
        final Product savedProduct2 = productRepository.insertProduct(PRODUCT_2);
        final CartItem savedCartItem1 = cartItemDao.save(new CartItem(2, MEMBER_3, savedProduct1));
        final CartItem savedCartItem2 = cartItemDao.save(new CartItem(3, MEMBER_3, savedProduct2));
        final OrderRequestDto orderRequestDto =
                new OrderRequestDto(List.of(savedCartItem1.getId(), savedCartItem2.getId()), null);
        //when
        final Long orderId = orderService.order(MEMBER_3, orderRequestDto);

        //then
        //장바구니에 주문한 아이템들 삭제했는지 검증
        final List<CartItem> member3CartItems = cartItemDao.findByMemberId(MEMBER_3.getId());
        assertThat(member3CartItems)
                .doesNotContain(savedCartItem1, savedCartItem2);

        //주문 내역에 저장했는지 검증
        final Order orderHistory = orderRepository.findById(orderId);
        assertThat(orderHistory.getOrderProducts())
                .extracting(OrderProduct::getProduct, OrderProduct::getQuantity)
                .containsExactlyInAnyOrder(
                        tuple(savedCartItem1.getProduct(), savedCartItem1.getQuantity()),
                        tuple(savedCartItem2.getProduct(), savedCartItem2.getQuantity())
                );
    }
}
