package cart.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.infrastructure.dao.CartItemDao;
import cart.infrastructure.entity.CartItemEntity;
import cart.infrastructure.entity.MemberEntity;
import cart.infrastructure.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CartItemRepositoryTest {

    @InjectMocks
    private JdbcCartItemRepository cartItemRepository;

    @Mock
    private CartItemDao cartItemDao;

    @Test
    void 장바구니에_상품을_저장한다() {
        // given
        given(cartItemDao.save(any()))
                .willReturn(1L);

        // when
        CartItem cartItem = cartItemRepository.save(new CartItem(
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                new Member(1L, "email@email.com", "password")
        ));

        // then
        assertThat(cartItem.getId()).isEqualTo(1L);
    }

    @Test
    void 장바구니_상품을_사용자_id로_조회한다() {
        // given
        MemberEntity memberEntity = new MemberEntity(1L, "email@email.com", "password");
        given(cartItemDao.findByMemberId(1L))
                .willReturn(List.of(
                        new CartItemEntity(
                                1L,
                                new ProductEntity(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                                memberEntity,
                                1
                        ),
                        new CartItemEntity(
                                2L,
                                new ProductEntity(2L, "박스터", BigDecimal.valueOf(100), "http://boxster.com"),
                                memberEntity,
                                1
                        )
                ));

        // when
        List<CartItem> cartItems = cartItemRepository.findAllByMemberId(memberEntity.getId());

        // then
        assertAll(
                () -> assertThat(cartItems).hasSize(2),
                () -> assertThat(cartItems).map(CartItem::getId).containsExactly(1L, 2L)
        );
    }

    @Test
    void 장바구니_상품을_장바구니_id로_조회한다() {
        // given
        MemberEntity memberEntity = new MemberEntity(1L, "email@email.com", "password");
        given(cartItemDao.findById(1L))
                .willReturn(Optional.of(new CartItemEntity(
                        1L,
                        new ProductEntity(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                        memberEntity,
                        1
                )));

        // when
        Optional<CartItem> cartItem = cartItemRepository.findById(1L);

        // then
        assertThat(cartItem.get().getProduct().getId()).isEqualTo(1L);
    }

    @Test
    void 장바구니_상품을_수정한다() {
        // given
        CartItem cartItem = new CartItem(
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                new Member(1L, "email@email.com", "password")
        );

        // when
        cartItemRepository.updateQuantity(cartItem);

        // then
        verify(cartItemDao, times(1)).updateQuantity(any());
    }

    @Test
    void 장바구니_상품을_삭제한다() {
        // given
        CartItem cartItem = new CartItem(
                1L,
                1,
                new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                new Member(1L, "email@email.com", "password")
        );

        // when
        cartItemRepository.deleteById(cartItem.getId());

        // then
        verify(cartItemDao, times(1)).deleteById(cartItem.getId());
    }
}
