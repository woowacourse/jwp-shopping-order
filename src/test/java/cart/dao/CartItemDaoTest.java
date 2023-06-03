package cart.dao;

import cart.domain.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.common.fixture.DomainFixture.EMAIL;
import static cart.common.fixture.DomainFixture.PASSWORD;
import static cart.common.fixture.DomainFixture.PRODUCT_IMAGE;
import static cart.common.fixture.DomainFixture.PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql(value = "classpath:test_truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;
    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);

        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);

        product = new Product(1L, PRODUCT_NAME, 20000, PRODUCT_IMAGE);
        member = new Member(1L, EMAIL, PASSWORD, 1000);

        final Long memberID = memberDao.addMember(new MemberEntity(EMAIL, PASSWORD, 1000));
        System.out.println(memberID);
        productDao.createProduct(product);
    }

    @Test
    void 장바구니_상품_정보를_저장한다() {
        //given
        final CartItem cartItem = new CartItem(member, product);

        //when
        final Long id = cartItemDao.save(cartItem);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void id로_장바구니_상품_정보를_찾는다() {
        //given
        final Long id = cartItemDao.save(new CartItem(member, product));

        //when
        final CartItem cartItem = cartItemDao.findById(id);

        //then
        assertThat(cartItem).isEqualTo(new CartItem(
                        1L,
                        1,
                        new Product(1L, PRODUCT_NAME, 20000, PRODUCT_IMAGE),
                        new Member(1L, EMAIL, PASSWORD, 1000)
                )
        );
    }

    @Test
    void id로_장바구니_상품_정보를_찾을_떄_입력한_id의_장바구니_상품이_존재하지_않으면_예외를_던진다() {
        //given
        final Long wrongId = Long.MIN_VALUE;

        //expect
        assertThatThrownBy(() -> cartItemDao.findById(wrongId))
                .isInstanceOf(CartItemException.IllegalId.class)
                .hasMessage("회원의 장바구니에 해당 id의 상품이 존재하지 않습니다; id=-9223372036854775808");
    }

    @Test
    void 회원_id로_장바구니_상품_정보를_찾는다() {
        //given
        cartItemDao.save(new CartItem(member, product));

        //when
        final List<CartItem> cartItems = cartItemDao.findByMemberId(member.getId());

        //then
        assertThat(cartItems).isEqualTo(List.of(new CartItem(
                        1L,
                        1,
                        new Product(1L, PRODUCT_NAME, 20000, PRODUCT_IMAGE),
                        new Member(1L, EMAIL, PASSWORD, 1000)
                )
        ));
    }

    @Test
    void 회원_id와_상품_id로_장바구니_상품_정보를_삭제한다() {
        //given
        final Long id = cartItemDao.save(new CartItem(member, product));

        //when
        final int affectedRows = cartItemDao.delete(member.getId(), product.getId());

        //then
        assertSoftly(softly -> {
            softly.assertThat(affectedRows).isEqualTo(1);
            softly.assertThatThrownBy(() -> cartItemDao.findById(id))
                    .isInstanceOf(CartItemException.IllegalId.class)
                    .hasMessage("회원의 장바구니에 해당 id의 상품이 존재하지 않습니다; id=1");
        });
    }

    @Test
    void id로_장바구니_상품_정보를_삭제한다() {
        //given
        final Long id = cartItemDao.save(new CartItem(member, product));

        //when
        final int affectedRows = cartItemDao.deleteById(id);

        //then
        assertSoftly(softly -> {
            softly.assertThat(affectedRows).isEqualTo(1);
            softly.assertThatThrownBy(() -> cartItemDao.findById(id))
                    .isInstanceOf(CartItemException.IllegalId.class)
                    .hasMessage("회원의 장바구니에 해당 id의 상품이 존재하지 않습니다; id=1");
        });
    }

    @Test
    void 장바구니_상품의_수량을_수정한다() {
        //given
        final Long id = cartItemDao.save(new CartItem(member, product));
        final CartItem cartItem = new CartItem(id, 2, product, member);

        //when
        final int affectedRows = cartItemDao.updateQuantity(cartItem);

        //then
        assertSoftly(softly -> {
            softly.assertThat(affectedRows).isEqualTo(1);
            softly.assertThat(cartItemDao.findById(id))
                    .isEqualTo(new CartItem(
                                    1L,
                                    2,
                                    new Product(1L, PRODUCT_NAME, 20000, PRODUCT_IMAGE),
                                    new Member(1L, EMAIL, PASSWORD, 1000)
                            )
                    );
        });
    }
}
