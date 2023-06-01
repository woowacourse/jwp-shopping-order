package cart.application;

import cart.application.request.OrderRequest;
import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.OrderHistory;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;
import cart.exception.OrderException;
import cart.fixture.dao.CartItemDaoFixture;
import cart.fixture.dao.MemberDaoFixture;
import cart.fixture.dao.OrderDaoFixture;
import cart.fixture.dao.ProductDaoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql("/truncate.sql")
@RecordApplicationEvents
@AutoConfigureTestDatabase
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    MemberDaoFixture memberDaoFixture;
    ProductDaoFixture productDaoFixture;
    CartItemDaoFixture cartItemDaoFixture;
    OrderDaoFixture orderDaoFixture;

    @BeforeEach
    void setUp(
            @Autowired MemberDao memberDao,
            @Autowired ProductDao productDao,
            @Autowired CartItemDao cartItemDao,
            @Autowired OrderDao orderDao
    ) {
        memberDaoFixture = new MemberDaoFixture(memberDao);
        productDaoFixture = new ProductDaoFixture(productDao);
        cartItemDaoFixture = new CartItemDaoFixture(cartItemDao);
        orderDaoFixture = new OrderDaoFixture(orderDao);
    }

    @Test
    void 주문시_사용_예정_포인트가_부족하면_예외가_발생한다() {
        // when
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);

        OrderRequest 주문_장바구니_상품_식별자값_목록 = new OrderRequest(List.of(장바구니_계란.getId()), 1001);

        // expect
        assertThatThrownBy(() -> orderService.createOrder(회원, 주문_장바구니_상품_식별자값_목록))
                .isInstanceOf(OrderException.NotEnoughPoint.class);
    }

    @Test
    void 주문시_결제할_금액이_부족하면_예외가_발생한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "5000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);

        // when
        OrderRequest 주문_장바구니_상품_식별자값_목록 = new OrderRequest(List.of(장바구니_계란.getId()), 100);

        // then
        assertThatThrownBy(() -> orderService.createOrder(회원, 주문_장바구니_상품_식별자값_목록))
                .isInstanceOf(OrderException.NotEnoughMoney.class);
    }

    @Test
    void 주문에_성공한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "100000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);

        OrderRequest 주문_요청 = new OrderRequest(List.of(장바구니_계란.getId()), 1000);

        // when
        Long 주문_식별자값 = orderService.createOrder(회원, 주문_요청);

        OrderHistory 주문_기록 = orderDaoFixture.주문을_조회한다(주문_식별자값);

        // then
        assertThat(주문_기록)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new OrderHistory(
                        주문_식별자값,
                        new Member(회원.getId(), "a@a.com", "1234", Money.from(90000), Money.from(90)),
                        OrderItems.from(List.of( new OrderItem(null, 계란, Quantity.from(10), 회원.getId()))),
                        Money.from("10000"),
                        Money.from("1000"),
                        null
                ));
    }
}
