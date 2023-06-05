package cart.dao;

import static cart.fixture.MemberFixture.사용자1_엔티티;
import static cart.fixture.MemberFixture.사용자2_엔티티;
import static cart.fixture.ProductFixture.상품_18900원_엔티티;
import static cart.fixture.ProductFixture.상품_28900원_엔티티;
import static cart.fixture.ProductFixture.상품_8900원_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@RepositoryTest
class CartItemDaoTest {

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final ProductEntity product = productDao.insert(상품_8900원_엔티티);
        final CartItemEntity cartItemEntity = new CartItemEntity(member.getId(), product.getId(), 1);

        // when
        cartItemDao.insert(cartItemEntity);

        // then
        assertThat(cartItemDao.findAllByMemberId(member.getId())).hasSize(1);
    }

    @Test
    void 사용자_아이디를_받아_해당_사용자의_장바구니에_있는_모든_품목을_반환한다() {
        // given
        final MemberEntity member1 = memberDao.insert(사용자1_엔티티);
        final MemberEntity member2 = memberDao.insert(사용자2_엔티티);
        final ProductEntity product1 = productDao.insert(상품_8900원_엔티티);
        final ProductEntity product2 = productDao.insert(상품_18900원_엔티티);
        final ProductEntity product3 = productDao.insert(상품_28900원_엔티티);

        cartItemDao.insert(new CartItemEntity(member1.getId(), product1.getId(), 1));
        cartItemDao.insert(new CartItemEntity(member1.getId(), product2.getId(), 1));
        cartItemDao.insert(new CartItemEntity(member2.getId(), product3.getId(), 1));

        // when
        final List<CartItemEntity> result = cartItemDao.findAllByMemberId(member1.getId());

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 사용자_아이디와_삭제할_장바구니의_상품_아이디를_받아_장바구니_항목을_제거한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final ProductEntity product = productDao.insert(상품_8900원_엔티티);
        final CartItemEntity cartItem = cartItemDao.insert(new CartItemEntity(member.getId(), product.getId(), 1));

        // when
        cartItemDao.deleteById(cartItem.getId());

        // then
        assertThat(cartItemDao.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 사용자_아이디와_삭제할_장바구니의_상품_아이디_목록을_받아_장바구니_항목을_제거한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final ProductEntity product1 = productDao.insert(상품_8900원_엔티티);
        final ProductEntity product2 = productDao.insert(상품_18900원_엔티티);
        final CartItemEntity cartItemEntity1 = cartItemDao.insert(
                new CartItemEntity(member.getId(), product1.getId(), 1)
        );
        final CartItemEntity cartItemEntity2 = cartItemDao.insert(
                new CartItemEntity(member.getId(), product2.getId(), 1)
        );

        // when
        cartItemDao.deleteByIds(List.of(cartItemEntity1.getId(), cartItemEntity2.getId()));

        // then
        assertThat(cartItemDao.findAllByMemberId(member.getId())).isEmpty();
    }

    @Test
    void 장바구니의_상품의_수량을_변경한다() {
        // given
        final MemberEntity member = memberDao.insert(사용자1_엔티티);
        final ProductEntity product = productDao.insert(상품_8900원_엔티티);
        final CartItemEntity cartItemEntity = new CartItemEntity(member.getId(), product.getId(), 1);
        final CartItemEntity savedCartItemEntity = cartItemDao.insert(cartItemEntity);

        final CartItemEntity updatedCartItemEntity = new CartItemEntity(
                savedCartItemEntity.getId(),
                savedCartItemEntity.getMemberId(),
                savedCartItemEntity.getProductId(),
                2
        );

        // when
        cartItemDao.updateQuantity(updatedCartItemEntity);

        // then
        final CartItemEntity result = cartItemDao.findById(updatedCartItemEntity.getId()).get();
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}
