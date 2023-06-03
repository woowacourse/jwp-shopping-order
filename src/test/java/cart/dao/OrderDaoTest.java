package cart.dao;

import cart.config.DaoTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.OrderHistory;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import cart.domain.vo.Quantity;
import cart.fixture.dao.CartItemDaoFixture;
import cart.fixture.dao.MemberDaoFixture;
import cart.fixture.dao.OrderItemDaoFixture;
import cart.fixture.dao.ProductDaoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cart.fixture.entity.OrderEntityFixture.주문_엔티티;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;


class OrderDaoTest extends DaoTestConfig {

    OrderDao orderDao;

    MemberDaoFixture memberDaoFixture;
    ProductDaoFixture productDaoFixture;
    CartItemDaoFixture cartItemDaoFixture;
    OrderItemDaoFixture orderItemDaoFixture;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        memberDaoFixture = new MemberDaoFixture(new MemberDao(jdbcTemplate));
        productDaoFixture = new ProductDaoFixture(new ProductDao(jdbcTemplate));
        cartItemDaoFixture = new CartItemDaoFixture(new CartItemDao(jdbcTemplate));
        orderItemDaoFixture = new OrderItemDaoFixture(new OrderItemDao(jdbcTemplate));
    }

    @Test
    void 주문을_저장한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "1000", "1000");

        // when
        Long 주문_식별자값 = orderDao.insertOrder(주문_엔티티(회원.getId(), "1000", "1000", "3000", now()));

        // then
        assertThat(주문_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 주문_식별자값으로_주문을_조회한다() {
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_상품 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);

        LocalDateTime 주문_시간 = now();
        Long 주문_식별자값 = orderDao.insertOrder(주문_엔티티(회원.getId(), "10000", "1000", "3000", 주문_시간));

        orderItemDaoFixture.주문_장바구니_상품을_등록한다(주문_식별자값, 장바구니_상품);

        // when
        Optional<OrderHistory> 아마도_주문_기록 = orderDao.getByOrderId(주문_식별자값);

        // then
        assertThat(아마도_주문_기록).contains(
                new OrderHistory(
                        주문_식별자값,
                        회원,
                        OrderItems.from(List.of(new OrderItem(계란, Quantity.from(10), 회원.getId()))),
                        Money.from("10000"),
                        Money.from("1000"),
                        주문_시간
                )
        );
    }

    @Test
    void 회원_식별자값을_통해_회원의_모든_주문_목록을_조회한다V2() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "100000", "1000");

        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        Product 치킨 = productDaoFixture.상품을_등록한다("치킨", 1000);
        Product 콜라 = productDaoFixture.상품을_등록한다("콜라", 1000);
        CartItem 장바구니1_상품 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);
        CartItem 장바구니2_상품 = cartItemDaoFixture.장바구니_상품을_등록한다(치킨, 회원, 10);
        CartItem 장바구니3_상품 = cartItemDaoFixture.장바구니_상품을_등록한다(콜라, 회원, 10);

        LocalDateTime 주문1_시간 = now();
        LocalDateTime 주문2_시간 = now();
        LocalDateTime 주문3_시간 = now();
        Long 주문1_식별자값 = orderDao.insertOrder(주문_엔티티(회원.getId(), "10000", "1000", "3000", 주문1_시간));
        Long 주문2_식별자값 = orderDao.insertOrder(주문_엔티티(회원.getId(), "10000", "0", "3000", 주문2_시간));
        Long 주문3_식별자값 = orderDao.insertOrder(주문_엔티티(회원.getId(), "10000", "0", "3000", 주문3_시간));

        Long 주문1_장바구니_식별자값 = orderItemDaoFixture.주문_장바구니_상품을_등록한다(주문1_식별자값, 장바구니1_상품);
        Long 주문2_장바구니_식별자값 = orderItemDaoFixture.주문_장바구니_상품을_등록한다(주문2_식별자값, 장바구니2_상품);
        Long 주문3_장바구니_식별자값 = orderItemDaoFixture.주문_장바구니_상품을_등록한다(주문3_식별자값, 장바구니3_상품);

        // when
        List<OrderHistory> 회원_주문_목록 = orderDao.getAllByMemberId(회원.getId());

        // then
        assertThat(회원_주문_목록)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(
                        new OrderHistory(
                                주문1_식별자값,
                                회원,
                                OrderItems.from(List.of(new OrderItem(주문1_장바구니_식별자값, 계란, Quantity.from(10), 회원.getId()))),
                                Money.from("10000"),
                                Money.from("1000"),
                                주문1_시간
                        ),
                        new OrderHistory(
                                주문2_식별자값,
                                회원,
                                OrderItems.from(List.of(new OrderItem(주문2_장바구니_식별자값, 치킨, Quantity.from(10), 회원.getId()))),
                                Money.from("10000"),
                                Money.from("0"),
                                주문2_시간
                        ),
                        new OrderHistory(
                                주문3_식별자값,
                                회원,
                                OrderItems.from(List.of(new OrderItem(주문3_장바구니_식별자값, 콜라, Quantity.from(10), 회원.getId()))),
                                Money.from("10000"),
                                Money.from("0"),
                                주문3_시간
                        )
                );
    }
}
