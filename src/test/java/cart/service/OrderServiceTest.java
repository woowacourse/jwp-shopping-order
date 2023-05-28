package cart.service;

import static cart.TestDataFixture.CART_ITEM_1;
import static cart.TestDataFixture.CART_ITEM_2;
import static cart.TestDataFixture.MEMBER_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.controller.request.OrderRequestDto;
import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Quantity;
import cart.repository.OrderRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import({OrderService.class, OrderRepository.class, OrderDao.class
        , OrderProductDao.class, ProductDao.class, MemberDao.class, CartItemDao.class})
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemDao cartItemDao;

    @DisplayName("상품을 주문하는 기능 테스트")
    @Test
    void orderProduct() {
        //given
        final OrderRequestDto orderRequestDto =
                new OrderRequestDto(List.of(CART_ITEM_1.getId(), CART_ITEM_2.getId()));

        //when
        final Long orderId = orderService.order(MEMBER_3, orderRequestDto);

        //then
        //장바구니에 주문한 아이템들 삭제했는지 검증
        final List<CartItem> member3CartItems = cartItemDao.findByMemberId(MEMBER_3.getId());
        assertThat(member3CartItems)
                .doesNotContain(CART_ITEM_1, CART_ITEM_2);

        //주문 내역에 저장했는지 검증
        final Order orderHistory = orderRepository.findById(orderId);
        assertThat(orderHistory.getOrderProducts())
                .extracting(OrderProduct::getProduct, OrderProduct::getQuantity)
                .containsExactlyInAnyOrder(
                        tuple(CART_ITEM_1.getProduct(), new Quantity(CART_ITEM_1.getQuantity())),
                        tuple(CART_ITEM_2.getProduct(), new Quantity(CART_ITEM_2.getQuantity()))
                );
    }
}
