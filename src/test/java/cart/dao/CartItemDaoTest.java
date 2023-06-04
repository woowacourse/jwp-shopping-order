package cart.dao;

import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.ProductFixture.PRODUCT_1;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.dao.dto.PageInfo;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;
import cart.domain.Product;
import cart.entity.CartItemEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CartItemDao cartItemDao;
    private MemberDao memberDao;
    private ProductDao productDao;
    private Member member;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        cartItemDao = new CartItemDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);

        Long memberId = memberDao.addMember(MEMBER_1);
        member = new Member(memberId, MEMBER_1.getEmail(), MEMBER_1.getPassword());

        Long productId = productDao.createProduct(PRODUCT_1);
        product = new Product(productId, PRODUCT_1.getName(), PRODUCT_1.getPrice(), PRODUCT_1.getImageUrl());

        cartItem = new CartItem(member, product);
    }

    @Test
    @DisplayName("특정 사용자의 장바구니 상품을 조회한다.")
    void findByMemberId() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when
        List<CartItemEntity> entities = cartItemDao.findByMemberId(member.getId());

        // then
        assertThat(entities).hasSize(1);
    }

    @Test
    @DisplayName("장바구니에 상품을 추가한다.")
    void save() {
        // when
        Long id = cartItemDao.save(cartItem);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("ID로 장바구니 상품을 조회한다.")
    void findById() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when
        CartItemEntity cartItemEntity = cartItemDao.findById(id);

        // then
        assertThat(cartItemEntity)
                .usingRecursiveComparison()
                .isEqualTo(new CartItemEntity(id,
                        cartItem.getMember().getId(),
                        cartItem.getProduct().getId(),
                        cartItem.getQuantity()));
    }

    @Test
    @DisplayName("장바구니 상품을 삭제한다.")
    void delete() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when
        cartItemDao.delete(member.getId(), product.getId());

        // then
        boolean isExist = cartItemDao.existsByProductIdAndMemberId(product.getId(), member.getId());
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("ID로 장바구니 상품을 삭제한다.")
    void deleteById() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when, then
        assertDoesNotThrow(
                () -> cartItemDao.deleteById(id)
        );
    }

    @Test
    @DisplayName("장바구니 상품의 수량을 변경한다.")
    void updateQuantity() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when
        CartItem newCartItem = new CartItem(id, cartItem.getQuantity() + 1, product, member);
        cartItemDao.updateQuantity(newCartItem);
        CartItemEntity entity = cartItemDao.findById(id);

        // then
        assertThat(entity)
                .extracting(CartItemEntity::getQuantity)
                .isEqualTo(cartItem.getQuantity() + 1);
    }

    @Test
    @DisplayName("특정 장바구니 상품들을 삭제한다.")
    void deleteAll() {
        // given
        Long id = cartItemDao.save(cartItem);
        CartItems cartItems = new CartItems(List.of(new CartItem(id, cartItem.getQuantity(), product, member)));

        // when, then
        assertDoesNotThrow(
                () -> cartItemDao.deleteAll(cartItems)
        );
    }

    @Test
    @DisplayName("장바구니 상품이 존재하는지 확인한다.")
    void existsByMemberIdAndProductId() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when
        boolean isExist = cartItemDao.existsByProductIdAndMemberId(product.getId(), member.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("장바구니 아이템들을 조회한다.")
    void findCartItemsByPage() {
        // given
        Long id = cartItemDao.save(cartItem);

        // when
        List<CartItemEntity> cartItemEntities = cartItemDao.findCartItemsByPage(member.getId(), 1, 1);

        // then
        assertAll(
                () -> assertThat(cartItemEntities)
                        .hasSize(1),
                () -> assertThat(cartItemEntities.get(0))
                        .usingRecursiveComparison()
                        .isEqualTo(new CartItemEntity(id,
                                cartItem.getMember().getId(),
                                cartItem.getProduct().getId(),
                                cartItem.getQuantity()))
        );
    }

    @Test
    @DisplayName("장바구니 아이템을 조회할 때 필요한 페이지 정보를 조회한다.")
    void findPageInfo() {
        // given
        cartItemDao.save(cartItem);

        // when
        PageInfo pageInfo = cartItemDao.findPageInfo(member.getId(), 1, 1);

        // then
        assertThat(pageInfo)
                .usingRecursiveComparison()
                .isEqualTo(new PageInfo(1, 1, 1, 1));
    }
}
