package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.CartItemEntity;
import cart.dao.entity.CartItemWithMemberAndProductEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartItemDaoTest {

    private final MemberEntity 멤버_엔티티 = new MemberEntity(
            null, "vero@email", "password", 20000, null, null
    );
    private final ProductEntity 상품_엔티티 = new ProductEntity(null,
            "치킨", 10_000, "http://example.com/chicken.jpg", null, null
    );

    private final ProductEntity 두번째_상품_엔티티 = new ProductEntity(null,
            "피자", 20_000, "http://example.com/pizza.jpg", null, null
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CartItemDao cartItemDao;
    private ProductDao productDao;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 장바구니_상품_ID로_상품을_조회한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity ID가_있는_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        CartItemEntity 장바구니_상품 = new CartItemEntity(null, 멤버.getId(), ID가_있는_상품.getId(), 10, null, null);
        Long 장바구니_상품_ID = cartItemDao.save(장바구니_상품);

        // when
        CartItemWithMemberAndProductEntity 장바구니_조회_상품 = cartItemDao.findById(장바구니_상품_ID).get();

        // then
        assertAll(
                () -> assertThat(장바구니_조회_상품.getProductEntity()).isEqualTo(ID가_있는_상품),
                () -> assertThat(장바구니_조회_상품.getMemberEntity()).isEqualTo(멤버)
        );
    }

    private MemberEntity 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 저장된_멤버_ID = memberDao.save(멤버_엔티티);
        return memberDao.findById(저장된_멤버_ID).get();
    }

    private ProductEntity 상품을_저장하고_ID를_갖는_상품을_리턴한다(ProductEntity 상품_엔티티) {
        Long 저장된_상품_ID = productDao.save(상품_엔티티);
        return productDao.findById(저장된_상품_ID).get();
    }

    @Test
    void 멤버_ID로_장바구니_상품을_조회한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity ID가_있는_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        ProductEntity ID가_있는_두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);

        CartItemEntity ID가_있는_장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(멤버, ID가_있는_상품);
        CartItemEntity ID가_있는_두번째_장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(멤버, ID가_있는_두번째_상품);

        // when
        List<CartItemWithMemberAndProductEntity> 장바구니_상품들 = cartItemDao.findByMemberId(멤버.getId());

        // then
        assertAll(
                () -> assertThat(장바구니_상품들).hasSize(2),
                () -> assertThat(장바구니_상품들)
                        .extracting(CartItemWithMemberAndProductEntity::getCartItemEntity)
                        .contains(ID가_있는_장바구니_상품, ID가_있는_두번째_장바구니_상품)
        );
    }

    private CartItemEntity 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(MemberEntity 멤버, ProductEntity 상품) {
        CartItemEntity 장바구니_상품 = new CartItemEntity(null, 멤버.getId(), 상품.getId(), 10, null, null);
        Long 장바구니_상품_ID = cartItemDao.save(장바구니_상품);

        return new CartItemEntity(
                장바구니_상품_ID, 장바구니_상품.getMemberId(), 장바구니_상품.getProductId(), 장바구니_상품.getQuantity(),
                장바구니_상품.getCreatedAt(), 장바구니_상품.getUpdatedAt()
        );
    }

    @Test
    void 장바구니_상품_ID로_장바구니_상품을_삭제한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity ID가_있는_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        CartItemEntity ID가_있는_장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(멤버, ID가_있는_상품);

        // when
        cartItemDao.deleteById(ID가_있는_장바구니_상품.getId());

        // then
        assertThat(cartItemDao.findById(ID가_있는_장바구니_상품.getId())).isEmpty();
    }

    @Test
    void 장바구니_상품의_수량을_변경한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity ID가_있는_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        CartItemEntity ID가_있는_장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(멤버, ID가_있는_상품);

        int 수량_증가량 = 20;
        CartItemEntity 수량이_변경된_장바구니_상품 = new CartItemEntity(
                ID가_있는_장바구니_상품.getId(), ID가_있는_장바구니_상품.getMemberId(), ID가_있는_장바구니_상품.getProductId(),
                ID가_있는_장바구니_상품.getQuantity() + 수량_증가량, ID가_있는_장바구니_상품.getCreatedAt(), ID가_있는_장바구니_상품.getUpdatedAt()
        );

        // when
        cartItemDao.updateQuantity(수량이_변경된_장바구니_상품);
        CartItemWithMemberAndProductEntity 조회한_장바구니_상품 = cartItemDao.findById(수량이_변경된_장바구니_상품.getId()).get();

        // then
        assertThat(조회한_장바구니_상품.getCartItemEntity().getQuantity()).isEqualTo(수량이_변경된_장바구니_상품.getQuantity());
    }

    @Test
    void 멤버_ID로_상품을_삭제한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity ID가_있는_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        ProductEntity ID가_있는_두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);

        장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(멤버, ID가_있는_상품);
        장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(멤버, ID가_있는_두번째_상품);

        // when
        cartItemDao.deleteByMemberId(멤버.getId());

        // then
        assertThat(cartItemDao.findByMemberId(멤버.getId())).isEmpty();
    }
}
