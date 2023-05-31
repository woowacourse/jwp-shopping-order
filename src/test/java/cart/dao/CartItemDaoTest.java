package cart.dao;

import cart.config.DaoTestConfig;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.vo.Money;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static cart.fixture.CartItemEntityFixture.장바구니_상품_엔티티;
import static cart.fixture.MemberEntityFixture.회원_엔티티;
import static cart.fixture.ProductEntityFixture.상품_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

class CartItemDaoTest extends DaoTestConfig {

    CartItemDao cartItemDao;
    MemberDao memberDao;
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 장바구니_상품을_저장한다() {
        // given
        MemberEntity 회원_엔티티 = 회원_엔티티("a@a.com", "1234", "1000", "1000");
        ProductEntity 계란_엔티티 = 상품_엔티티("계란", 1000);

        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티);
        Long 계란_식별자값 = productDao.insertProduct(계란_엔티티);

        CartItemEntity 장바구니_상품_엔티티 = 장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10);

        // when
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티);

        // then
        assertThat(장바구니_계란_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 장바구니_상품_식별자값으로_장바구니_상품을_조회한다() {
        // given
        MemberEntity 회원_엔티티 = 회원_엔티티("a@a.com", "1234", "1000", "1000");
        ProductEntity 계란_엔티티 = 상품_엔티티("계란", 1000);

        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티);
        Long 계란_식별자값 = productDao.insertProduct(계란_엔티티);

        CartItemEntity 장바구니_상품_엔티티 = 장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10);
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티);

        // when
        Optional<CartItem> 아마_장바구니_계란 = cartItemDao.findByCartItemId(장바구니_계란_식별자값);

        // then
        assertThat(아마_장바구니_계란).contains(
                new CartItem(
                        장바구니_계란_식별자값,
                        new Product(계란_식별자값, "계란", 1000, "https://image.png"),
                        new Member(회원_식별자값, "a@a.com", "1234", Money.from(1000), Money.from(1000)),
                        10
                )
        );
    }

    @Test
    void 회원_식별자값으로_장바구니_상품을_조회한다() {
        // given
        MemberEntity 회원_엔티티 = 회원_엔티티("a@a.com", "1234", "1000", "1000");
        ProductEntity 계란_엔티티 = 상품_엔티티("계란", 1000);

        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티);
        Long 계란_식별자값 = productDao.insertProduct(계란_엔티티);

        CartItemEntity 장바구니_상품_엔티티 = 장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10);
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티);

        // when
        List<CartItem> 회원_장바구니_상품_목록 = cartItemDao.findByMemberId(회원_식별자값);

        // then
        assertThat(회원_장바구니_상품_목록).contains(
                new CartItem(
                        장바구니_계란_식별자값,
                        new Product(계란_식별자값, "계란", 1000, "https://image.png"),
                        new Member(회원_식별자값, "a@a.com", "1234", Money.from(1000), Money.from(1000)),
                        10
                )
        );
    }

    @Test
    void 장바구니_상품_식별자값으로_장바구니_상품의_수량을_수정한다() {
        // given
        MemberEntity 회원_엔티티 = 회원_엔티티("a@a.com", "1234", "1000", "1000");
        ProductEntity 계란_엔티티 = 상품_엔티티("계란", 1000);

        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티);
        Long 계란_식별자값 = productDao.insertProduct(계란_엔티티);

        CartItemEntity 장바구니_상품_엔티티 = 장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10);
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티);

        // when
        cartItemDao.updateQuantity(장바구니_계란_식별자값, 5);

        Optional<CartItem> 아마_장바구니_계란 = cartItemDao.findByCartItemId(장바구니_계란_식별자값);

        // then
        assertThat(아마_장바구니_계란).contains(
                new CartItem(
                        장바구니_계란_식별자값,
                        new Product(계란_식별자값, "계란", 1000, "https://image.png"),
                        new Member(회원_식별자값, "a@a.com", "1234", Money.from(1000), Money.from(1000)),
                        5
                )
        );
    }

    @Test
    void 장바구니_상품_식별자값으로_장바구니_상품을_삭제한다() {
        // given
        MemberEntity 회원_엔티티 = 회원_엔티티("a@a.com", "1234", "1000", "1000");
        ProductEntity 계란_엔티티 = 상품_엔티티("계란", 1000);

        Long 회원_식별자값 = memberDao.insertMember(회원_엔티티);
        Long 계란_식별자값 = productDao.insertProduct(계란_엔티티);

        CartItemEntity 장바구니_상품_엔티티 = 장바구니_상품_엔티티(계란_식별자값, 회원_식별자값, 10);
        Long 장바구니_계란_식별자값 = cartItemDao.insertCartItem(장바구니_상품_엔티티);

        // when
        cartItemDao.deleteByCartItemId(장바구니_계란_식별자값);

        Optional<CartItem> 아마_장바구니_계란 = cartItemDao.findByCartItemId(장바구니_계란_식별자값);

        // then
        assertThat(아마_장바구니_계란).isEmpty();
    }
}
