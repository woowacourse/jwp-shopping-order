package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@JdbcTest
class CartItemDaoTest {

    private CartItemDao cartItemDao;
    private MemberDao memberDao;
    private ProductDao productDao;

    private Member member;
    private Product product;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);

        memberDao.addMember(new Member("abc@naver.com", "1234"));
        member = memberDao.getMemberByEmail("abc@naver.com");

        final Long productId = productDao.createProduct(new Product("연어", 50000, "이미지url"));
        product = new Product(productId, "연어", 50000, "이미지url");
    }

    @DisplayName("여러 ID들로 조회할 수 있다")
    @Test
    void findAllByIds() {
        //given
        final Long id1 = cartItemDao.save(new CartItem(member, product));
        final Long id2 = cartItemDao.save(new CartItem(member, product));
        final Long id3 = cartItemDao.save(new CartItem(member, product));
        final Long id4 = cartItemDao.save(new CartItem(member, product));

        //when
        final List<CartItem> cartItems = cartItemDao.findAllByIds(List.of(id1, id2, id3, id4));

        //then
        assertThat(cartItems).hasSize(4);
    }

    @DisplayName("여러 ID들을 모두 삭제할 수 있다")
    @Test
    void deleteAllByIds() {
        //given
        final Long id1 = cartItemDao.save(new CartItem(member, product));
        final Long id2 = cartItemDao.save(new CartItem(member, product));
        final Long id3 = cartItemDao.save(new CartItem(member, product));
        final Long id4 = cartItemDao.save(new CartItem(member, product));

        final int beforeDelete = countRowsInTable(jdbcTemplate, "cart_item");

        //when
        cartItemDao.deleteAllByIds(List.of(id1, id2, id3, id4));
        final int afterDelete = countRowsInTable(jdbcTemplate, "cart_item");

        //then
        assertThat(beforeDelete).isEqualTo(afterDelete + 4);
    }
}
