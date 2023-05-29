package cart.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dto.AuthMember;
import cart.dto.CartItemRequest;
import cart.exception.ProductNotFoundException;
import cart.fixtures.MemberFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    @DisplayName("추가할 상품 ID가 존재하지 않으면 예외가 발생한다.")
    void add_throws_when_productId_notExist() {
        // given
        AuthMember authMember = new AuthMember(MemberFixtures.Dooly.EMAIL, MemberFixtures.Dooly.PASSWORD);
        Long notExistId = -1L;
        CartItemRequest request = new CartItemRequest(notExistId, 2);
        given(productDao.isNotExistById(notExistId)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> cartItemService.add(authMember, request))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
    }
}
