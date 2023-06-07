package cart.repository;

import cart.config.RepositoryTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderHistory;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.domain.CartItemsFixture.장바구니_상품_목록;
import static cart.fixture.domain.MoneyFixture.배송비;
import static cart.fixture.domain.MoneyFixture.포인트;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest extends RepositoryTestConfig {

    OrderRepository orderRepository;

    Member 회원;
    Product 계란;
    CartItem 장바구니_계란;
    CartItems 장바구니_상품_목록;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(orderDao, orderItemDao);
        회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
        계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);
        장바구니_상품_목록 = 장바구니_상품_목록(List.of(장바구니_계란));
    }

    @Test
    void 주문과_주문_상품_목록을_저장한다() {
        // given
        Order 주문 = new Order(회원, 장바구니_상품_목록, 포인트("1000"), 배송비("3000"));

        // when
        Long 주문_식별자값 = orderRepository.saveOrder(주문);

        // then
        assertThat(주문_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 회원의_주문을_조회한다() {
        // given
        Order 주문 = new Order(회원, 장바구니_상품_목록, 포인트("1000"), 배송비("3000"));

        Long 주문_식별자값 = orderRepository.saveOrder(주문);

        // when
        List<OrderHistory> 회원_주문_기록_목록 = orderRepository.findOrdersByMemberId(회원.getId());

        // then
        assertThat(회원_주문_기록_목록)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new OrderHistory(주문_식별자값,
                                회원,
                                OrderItems.from(List.of(new OrderItem(null, 계란, Quantity.from(10), 회원.getId()))),
                                Money.from("10000"),
                                Money.from("1000"),
                                Money.from("3000"),
                                null
                        )
                ));
    }
}
