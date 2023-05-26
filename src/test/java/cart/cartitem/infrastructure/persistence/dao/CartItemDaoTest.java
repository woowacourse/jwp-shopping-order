package cart.cartitem.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.cartitem.infrastructure.persistence.entity.CartItemEntity;
import cart.common.DaoTest;
import cart.member.domain.Member;
import cart.member.infrastructure.persistence.dao.MemberDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItemDao 은(는)")
@DaoTest
class CartItemDaoTest {

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private MemberDao memberDao;

    private Long memberId;

    @BeforeEach
    void setUp() {
        memberDao.addMember(new Member(null, "email", "1234"));
        memberId = memberDao.getMemberByEmail("email").getId();
    }

    @Test
    void 장바구니_상품을_저장한다() {
        // when
        CartItemEntity entity = new CartItemEntity(null, 10, 1L, "상품1", "image", 1000, memberId);
        Long id = cartItemDao.save(entity);

        // then
        CartItemEntity saved = cartItemDao.findById(id).get();
        assertThat(saved).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(entity);
    }

    @Test
    void 장바구니_상품의_수량을_변경한다() {
        // given
        CartItemEntity entity = new CartItemEntity(null, 10, 1L, "상품1", "image", 1000, memberId);
        Long id = cartItemDao.save(entity);
        CartItemEntity forUpdate = new CartItemEntity(id, 20, 1L, "상품1", "image", 1000, memberId);

        // when
        cartItemDao.updateQuantity(forUpdate);

        // then
        assertThat(cartItemDao.findById(id).get().getQuantity()).isEqualTo(20);
    }

    @Test
    void 장바구니_항목을_삭제한다() {
        // given
        CartItemEntity entity = new CartItemEntity(null, 10, 1L, "상품1", "image", 1000, memberId);
        Long id = cartItemDao.save(entity);

        // when
        cartItemDao.delete(memberId, 1L);

        // then
        assertThat(cartItemDao.findById(id)).isEmpty();
    }

    @Test
    void 장바구니_항목을_ID로_삭제한다() {
        // given
        CartItemEntity entity = new CartItemEntity(null, 10, 1L, "상품1", "image", 1000, memberId);
        Long id = cartItemDao.save(entity);

        // when
        cartItemDao.deleteById(id);

        // then
        assertThat(cartItemDao.findById(id)).isEmpty();
    }

    @Test
    void 특정_회원의_장바구니_항목을_조회한다() {
        // given
        CartItemEntity entity1 = new CartItemEntity(null, 10, 1L, "상품1", "image", 1000, memberId);
        CartItemEntity entity2 = new CartItemEntity(null, 20, 2L, "상품2", "image", 2000, memberId);
        cartItemDao.save(entity1);
        cartItemDao.save(entity2);

        // when
        List<CartItemEntity> byMemberId = cartItemDao.findByMemberId(memberId);

        // then
        assertThat(byMemberId).hasSize(2);
    }
}
