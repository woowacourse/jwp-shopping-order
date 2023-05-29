package cart.dao;

import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
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
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        CartItem cartItem = new CartItem(CHICKEN, MEMBER.getId());

        // when
        Long actual = cartItemDao.save(cartItem);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 아이디를_통해_아이템을_조회한다() {
        // given
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        CartItem expected = new CartItem(CHICKEN, MEMBER.getId());
        Long cartItemId = cartItemDao.save(expected);

        // when
        CartItem actual = cartItemDao.findById(cartItemId).get();

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    void 존재하지_않는_아이디로_조회시_빈값_반환() {
        // when
        Optional<CartItem> actual = cartItemDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 멤버_아이디를_통해_모든_아이템을_조회한다() {
        // given
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        insertProduct(PIZZA);
        cartItemDao.save(new CartItem(CHICKEN, MEMBER.getId()));
        cartItemDao.save(new CartItem(PIZZA, MEMBER.getId()));

        insertMember(new Member(2L, "bbb@kakao.naver.com", "password"));
        cartItemDao.save(new CartItem(CHICKEN, 2L));

        // when
        List<CartItem> actual = cartItemDao.findByMemberId(MEMBER.getId());

        // then
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void id를_통해_아이템을_삭제한다() {
        // given
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        Long cartItemId = cartItemDao.save(new CartItem(CHICKEN, MEMBER.getId()));

        // when
        cartItemDao.deleteById(cartItemId);

        // then
        assertThat(cartItemDao.findById(cartItemId)).isEmpty();
    }

    @Test
    void 아이템_수량을_변경한다() {
        // given
        insertMember(MEMBER);
        insertProduct(CHICKEN);
        Long cartItemId = cartItemDao.save(new CartItem(CHICKEN, MEMBER.getId()));
        CartItem expected = new CartItem(cartItemId, 10, CHICKEN, MEMBER.getId());

        // when
        cartItemDao.updateQuantity(expected);

        // then
        CartItem actual = cartItemDao.findById(cartItemId).get();
        assertThat(actual.getQuantity()).isEqualTo(10);
    }

    private void insertMember(Member member) {
        String memberSql = "INSERT INTO member (id, email, password) VALUES (?, ?,?)";
        jdbcTemplate.update(memberSql, member.getId(), member.getEmail(), member.getPassword());
    }

    private void insertProduct(Product product) {
        String productSql = "INSERT INTO Product (id, name, price, image_url) VALUES (?, ?,?,?)";
        jdbcTemplate.update(productSql, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
