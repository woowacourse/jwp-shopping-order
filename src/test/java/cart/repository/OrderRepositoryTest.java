package cart.repository;

import cart.config.RepositoryTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.product.Product;
import cart.domain.vo.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cart.fixture.domain.CartItemsFixture.장바구니_상품_목록;
import static cart.fixture.domain.OrderFixture.주문;
import static cart.fixture.entity.CartItemEntityFixture.장바구니_상품_엔티티;
import static cart.fixture.entity.MemberEntityFixture.회원_엔티티;
import static cart.fixture.entity.ProductEntityFixture.상품_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest extends RepositoryTestConfig {

    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(orderDao, orderItemDao);
    }

    @Test
    void 주문과_주문_상품_목록을_저장한다() {
        // given
        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티("a@a.com", "1234", "10000", "1000"));
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10));

        Member 회원 = memberDao.getByMemberId(회원_식별자값).get().toDomain();
        Product 계란 = productDao.getByProductId(계란_식별자값).get().toDomain();
        CartItem 장바구니_계란 = cartItemDao.findByCartItemId(장바구니_계란_식별자값).get();

        CartItems 장바구니_상품_목록 = 장바구니_상품_목록(List.of(장바구니_계란));

        Order 주문 = 주문(회원, 장바구니_상품_목록, Money.from(1000));

        // when
        Long 주문_식별자값 = orderRepository.saveOrder(주문);

        // then
        assertThat(주문_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Disabled
    @Test
    void 회원의_주문을_조회한다() {
        // given
        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티("a@a.com", "1234", "10000", "1000"));
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10));

        Member 회원 = memberDao.getByMemberId(회원_식별자값).get().toDomain();
        Product 계란 = productDao.getByProductId(계란_식별자값).get().toDomain();
        CartItem 장바구니_계란 = cartItemDao.findByCartItemId(장바구니_계란_식별자값).get();

        CartItems 장바구니_상품_목록 = 장바구니_상품_목록(List.of(장바구니_계란));

        Order 주문 = 주문(회원, 장바구니_상품_목록, Money.from(1000));
        Long 주문_식별자값 = orderRepository.saveOrder(주문);
//
//        // when
//        orderRepository.
//
//        // then
//        assertThat(주문_식별자값)
//                .isNotNull()
//                .isNotZero();
    }
}
