package cart.dao;

import cart.config.DaoTestConfig;
import cart.dao.entity.OrderItemEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.fixture.dao.CartItemDaoFixture;
import cart.fixture.dao.MemberDaoFixture;
import cart.fixture.dao.OrderDaoFixture;
import cart.fixture.dao.ProductDaoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static cart.fixture.domain.CartItemsFixture.장바구니_상품_목록;
import static cart.fixture.entity.OrderItemEntityFixture.주문의_장바구니_상품_엔티티;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class OrderItemDaoTest extends DaoTestConfig {

    OrderItemDao orderItemDao;

    OrderDaoFixture orderDaoFixture;
    ProductDaoFixture productDaoFixture;
    MemberDaoFixture memberDaoFixture;
    CartItemDaoFixture cartItemDaoFixture;

    @BeforeEach
    void setUp() {
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDaoFixture = new OrderDaoFixture(new OrderDao(jdbcTemplate));
        productDaoFixture = new ProductDaoFixture(new ProductDao(jdbcTemplate));
        memberDaoFixture = new MemberDaoFixture(new MemberDao(jdbcTemplate));
        cartItemDaoFixture = new CartItemDaoFixture(new CartItemDao(jdbcTemplate));
    }

    @Test
    void 주문의_장바구니_상품을_저장한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_상품 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);
        Long 주문_식별자값 = orderDaoFixture.주문을_등록한다(회원, 장바구니_상품_목록(List.of(장바구니_상품)), "1000", "3000", now());
        OrderItemEntity 주문의_장바구니_상품_엔티티 = 주문의_장바구니_상품_엔티티(주문_식별자값, 장바구니_상품);

        // when
        Long 주문의_장바구니_상품_식별자값 = orderItemDao.insertOrderItem(주문의_장바구니_상품_엔티티);

        // then
        assertThat(주문의_장바구니_상품_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 주문_식별자값을_통해_주문의_장바구니_상품_목록을_조회한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        CartItem 장바구니_상품 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);
        Long 주문_식별자값 = orderDaoFixture.주문을_등록한다(회원, 장바구니_상품_목록(List.of(장바구니_상품)), "1000", "3000", now());
        OrderItemEntity 주문의_장바구니_상품_엔티티 = 주문의_장바구니_상품_엔티티(주문_식별자값, 장바구니_상품);

        Long 주문의_장바구니_상품_식별자값 = orderItemDao.insertOrderItem(주문의_장바구니_상품_엔티티);

        // when
        List<OrderItemEntity> 주문의_장바구니_상품_목록 = orderItemDao.getByOrderId(주문_식별자값);

        // then
        assertThat(주문의_장바구니_상품_목록).contains(
                OrderItemEntity.from(주문_식별자값, 장바구니_상품)
        );
    }

    @Test
    void 주문할_장바구니_상품을_배치로_저장한다() {
        // given
        Member 회원 = memberDaoFixture.회원을_등록한다("a@a.com", "1234", "100000", "1000");
        Product 계란 = productDaoFixture.상품을_등록한다("계란", 1000);
        Product 치킨 = productDaoFixture.상품을_등록한다("치킨", 20000);

        CartItem 장바구니_계란 = cartItemDaoFixture.장바구니_상품을_등록한다(계란, 회원, 10);
        CartItem 장바구니_치킨 = cartItemDaoFixture.장바구니_상품을_등록한다(치킨, 회원, 1);

        LocalDateTime 주문_시간 = now();
        Long 주문_식별자값 = orderDaoFixture.주문을_등록한다(회원, 장바구니_상품_목록(List.of(장바구니_계란, 장바구니_치킨)), "1000", "3000", 주문_시간);

        OrderItemEntity 주문의_장바구니_계란_엔티티 = 주문의_장바구니_상품_엔티티(주문_식별자값, 장바구니_계란);
        OrderItemEntity 주문의_장바구니_치킨_엔티티 = 주문의_장바구니_상품_엔티티(주문_식별자값, 장바구니_치킨);

        // when
        orderItemDao.insertBatch(List.of(주문의_장바구니_계란_엔티티, 주문의_장바구니_치킨_엔티티));

        List<OrderItemEntity> 주문한_장바구니_상품_엔티티_목록 = orderItemDao.getByOrderId(주문_식별자값);
        // then
        assertThat(주문한_장바구니_상품_엔티티_목록)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        OrderItemEntity.from(주문_식별자값, 장바구니_계란),
                        OrderItemEntity.from(주문_식별자값, 장바구니_치킨)
                ));
    }
}
