package cart.dao;

import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertProduct;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.entity.CartItemEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItemDao 은(는)")
@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    void 장바구니에_아이템을_추가한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        CartItemEntity cartItemEntity = new CartItemEntity(CHICKEN.getId(), MEMBER.getId(), 2);

        // when
        Long actual = cartItemDao.save(cartItemEntity);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 아이디를_통해_아이템을_조회한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        CartItemEntity expected = new CartItemEntity(CHICKEN.getId(), MEMBER.getId(), 2);
        Long cartItemId = cartItemDao.save(expected);

        // when
        CartItemEntity actual = cartItemDao.findById(cartItemId).get();

        // then
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getMemberId()).isEqualTo(expected.getMemberId()),
                () -> assertThat(actual.getProductId()).isEqualTo(expected.getProductId()),
                () -> assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );

    }

    @Test
    void 존재하지_않는_아이디로_조회시_빈값_반환() {
        // when
        Optional<CartItemEntity> actual = cartItemDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 멤버_아이디를_통해_모든_아이템을_조회한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        CartItemEntity chickenCartItem = new CartItemEntity(CHICKEN.getId(), MEMBER.getId(), 2);
        CartItemEntity pizzaCartItem = new CartItemEntity(PIZZA.getId(), MEMBER.getId(), 5);

        cartItemDao.save(chickenCartItem);
        cartItemDao.save(pizzaCartItem);

        insertMember(new Member(2L, "bbb@kakao.naver.com", "password"), jdbcTemplate);
        cartItemDao.save(new CartItemEntity(CHICKEN.getId(), 2L, 10));

        // when
        List<CartItemEntity> actual = cartItemDao.findByMemberId(MEMBER.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void id를_통해_아이템을_삭제한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        CartItemEntity cartItemEntity = new CartItemEntity(CHICKEN.getId(), MEMBER.getId(), 2);
        Long cartItemId = cartItemDao.save(cartItemEntity);

        // when
        cartItemDao.deleteById(cartItemId);

        // then
        assertThat(cartItemDao.findById(cartItemId)).isEmpty();
    }

    @Test
    void 아이템_수량을_변경한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        CartItemEntity cartItemEntity = new CartItemEntity(CHICKEN.getId(), MEMBER.getId(), 2);
        Long cartItemId = cartItemDao.save(cartItemEntity);
        CartItemEntity expected = new CartItemEntity(cartItemId, CHICKEN.getId(), MEMBER.getId(), 10);

        // when
        cartItemDao.updateQuantity(expected);

        // then
        CartItemEntity actual = cartItemDao.findById(cartItemId).get();
        assertThat(actual.getQuantity()).isEqualTo(10);
    }

    @Test
    void 아이디_목록을_받아서_모두_삭제한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        CartItemEntity chickenCartItem = new CartItemEntity(CHICKEN.getId(), MEMBER.getId(), 2);
        CartItemEntity pizzaCartItem = new CartItemEntity(PIZZA.getId(), MEMBER.getId(), 5);

        List<Long> ids = List.of(
                cartItemDao.save(chickenCartItem),
                cartItemDao.save(pizzaCartItem));

        // when
        cartItemDao.deleteByIds(ids);

        // then
        List<CartItemEntity> result = cartItemDao.findByMemberId(MEMBER.getId());
        assertThat(result).isEmpty();
    }
}
