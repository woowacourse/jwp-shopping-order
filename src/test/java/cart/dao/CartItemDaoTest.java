package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.CartItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CartItemDaoTest {

    private final CartItemDao cartItemDao;
    private List<CartItemEntity> cartItems;
    private CartItemEntity cartItem;

    @Autowired
    public CartItemDaoTest(final JdbcTemplate jdbcTemplate) {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        cartItems = cartItemDao.findAll();
        cartItem = cartItems.get(0);
    }

    @DisplayName("주문 아이디, 회원 아이디가 일치하는 장바구니 상품 정보를 조회한다.")
    @Test
    void findByIdForMember() {
        // given
        final CartItemEntity cartItem = cartItems.get(0);
        final Long id = cartItem.getId();
        final long memberId = cartItem.getMemberId();

        // when
        final CartItemEntity found = cartItemDao.findByIdForMember(memberId, id).get();

        // then
        assertThat(found.getProductId()).isEqualTo(cartItem.getProductId());
        assertThat(found.getQuantity()).isEqualTo(cartItem.getQuantity());
    }

    @DisplayName("특정 회원의 모든 장바구니 상품 정보를 조회한다.")
    @Test
    void findByMemberId() {
        // given
        final long memberId = cartItem.getMemberId();

        // when
        final List<CartItemEntity> found = cartItemDao.findByMemberId(memberId);

        // then
        for (final CartItemEntity foundCartItem : found) {
            assertThat(foundCartItem.getMemberId())
                    .isEqualTo(memberId);
        }
    }

    @DisplayName("장바구니 상품 정보를 저장한다.")
    @Test
    void save() {
        // given
        final CartItemEntity toSave = new CartItemEntity(1L, 1L, 1);

        // when
        final long savedId = cartItemDao.save(toSave);

        // then
        cartItemDao.findByIdForMember(toSave.getMemberId(), savedId)
                .ifPresentOrElse(
                        found -> {
                            assertThat(found.getProductId()).isEqualTo(toSave.getProductId());
                            assertThat(found.getQuantity()).isEqualTo(toSave.getProductId());
                        },
                        () -> Assertions.fail("cartItem not exist; cartItemId=" + savedId));
    }

    @DisplayName("장바구니 아이디, 회원 아이디가 일치하는 장바구니 상품의 존재 여부를 반환한다.")
    @Test
    void isExist() {
        // given
        final Long id = cartItem.getId();
        final long memberId = cartItem.getMemberId();

        // when, then
        assertThat(cartItemDao.isExist(memberId, id)).isTrue();
    }

    @DisplayName("특정 아이디를 가진 장바구니 상품을 삭제한다.")
    @Test
    void deleteById() {
        // given
        final Long id = cartItem.getId();
        final long memberId = cartItem.getMemberId();

        // when
        cartItemDao.deleteById(id);

        // then
        assertThat(cartItemDao.findByIdForMember(memberId, id)).isEmpty();
    }

    @DisplayName("특정 아이디를 가진 복수개의 장바구니 상품을 삭제한다.")
    @Test
    void deleteByIds() {
        // given
        final List<Long> ids = cartItems.stream()
                .map(CartItemEntity::getId)
                .collect(Collectors.toList());

        // when
        cartItemDao.deleteByIds(ids);

        // then
        assertThat(cartItemDao.findAll()).isEmpty();
    }

    @DisplayName("특정 장바구니 상품의 수량을 변경한다.")
    @Test
    void updateQuantity() {
        // given
        final long id = cartItem.getId();
        final CartItemEntity toUpdate = new CartItemEntity(
                id,
                cartItem.getMemberId(),
                cartItem.getProductId(),
                3);

        // when
        cartItemDao.updateQuantity(toUpdate);

        // then
        cartItemDao.findByIdForMember(toUpdate.getMemberId(), id)
                .ifPresentOrElse(
                        found -> {
                            assertThat(found.getProductId()).isEqualTo(toUpdate.getProductId());
                            assertThat(found.getQuantity()).isEqualTo(toUpdate.getQuantity());
                        },
                        () -> Assertions.fail("cartItem not exist; cartItemId=" + id));
    }
}
