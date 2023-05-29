package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private CartItemDao cartItemDao;
    private ProductDao productDao;
    private Product product1;
    private Product product2;
    private Member member;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);

        this.member = findMemberById(1L);
        this.product1 = createProduct("치킨", 10_000, "http://example.com/chicken.jpg");
        this.product2 = createProduct("피자", 20_000, "http://example.com/pizza.jpg");

    }

    @Test
    void deleteByMemberIdAndProductIds() {
        //given
        final List<CartItem> beforeSave = cartItemDao.findByMemberId(member.getId());
        cartItemDao.save(new CartItem(member, product1));
        cartItemDao.save(new CartItem(member, product2));

        //when
        cartItemDao.delete(member.getId(), product1.getId());
        cartItemDao.delete(member.getId(), product2.getId());

        //then
        final List<CartItem> byMemberId = cartItemDao.findByMemberId(member.getId());
        assertThat(byMemberId).usingRecursiveComparison().isEqualTo(beforeSave);
    }

    private Member findMemberById(final Long memberId) {
        return memberDao.getMemberById(1L);
    }

    private Product createProduct(final String name, final int price, final String imageUrl) {
        final Product product = new Product(name, price, imageUrl);
        final Long productId = productDao.createProduct(product);
        return new Product(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

}
