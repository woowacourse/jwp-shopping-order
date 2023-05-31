package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.CartItemEntity;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class CartItemDaoTest extends DaoTest {

    private static final Member dummyMember = new Member(1L, "email1", "pw1", 1);
    private static final Product dummyProduct = new Product(1L, "name1", 1, "imageUrl1", 1);
    private static final CartItemEntity dummyCartItemEntity = new CartItemEntity(1L, 1L, 1);

    private MemberDao memberDao;
    private ProductDao productDao;
    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);

        insertDummyMemberAndProduct();
    }

    private void insertDummyMemberAndProduct() {
        memberDao.insert(dummyMember);
        productDao.insert(dummyProduct);
    }

    @Test
    void 데이터를_삽입한다() {
        // when
        long savedId = cartItemDao.insert(dummyCartItemEntity);

        // then
        assertThat(savedId).isEqualTo(1);
    }

    @Test
    void ID로_데이터를_조회한다() {
        // given
        long savedId = cartItemDao.insert(dummyCartItemEntity);

        // when
        CartItemEntity foundCartItemEntity = cartItemDao.findById(savedId);

        // then
        assertThat(foundCartItemEntity.getQuantity()).isEqualTo(1);
    }

    @Test
    void 모든_데이터를_조회한다() {
        // given
        cartItemDao.insert(dummyCartItemEntity);

        // when
        List<CartItemEntity> result = cartItemDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 회원ID로_데이터를_조회한다() {
        // given
        cartItemDao.insert(dummyCartItemEntity);

        // when
        List<CartItemEntity> result = cartItemDao.findByMemberId(dummyMember.getId());

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 존재하지_않는_데이터를_조회하면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> cartItemDao.findById(213213L))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    void 데이터를_수정한다() {
        // given
        long savedId = cartItemDao.insert(dummyCartItemEntity);

        CartItemEntity newCartItemEntity = new CartItemEntity(savedId, 1L, 1L, 2);

        // when
        cartItemDao.update(newCartItemEntity);

        // then
        CartItemEntity foundCartItemEntity = cartItemDao.findById(savedId);
        assertThat(foundCartItemEntity.getQuantity()).isEqualTo(2);
    }

    @Test
    void ID로_데이터를_삭제한다() {
        // given
        long savedId = cartItemDao.insert(dummyCartItemEntity);

        // when
        cartItemDao.deleteById(savedId);

        // then
        assertThatThrownBy(() -> cartItemDao.findById(savedId))
                .isInstanceOf(CartItemNotFoundException.class);
    }
}
