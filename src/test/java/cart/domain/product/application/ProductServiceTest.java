package cart.domain.product.application;

import static cart.fixtures.ProductFixtures.CHICKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import cart.domain.cartitem.persistence.CartItemDao;
import cart.domain.member.persistence.MemberDao;
import cart.domain.product.application.dto.ProductRequest;
import cart.domain.product.persistence.ProductDao;
import cart.fixtures.MemberFixtures.Dooly;
import cart.global.config.AuthMember;
import cart.global.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("찾을 상품 ID가 존재하지 않으면 예외가 발생한다.")
    void findProductCartItems_throws_when_productId_notExist() {
        // given
        AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
        Long notExistId = -1L;
        given(productDao.isNotExistById(notExistId)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> productService.findProductCartItems(authMember, notExistId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("삭제할 상품 ID가 존재하지 않으면 예외가 발생한다.")
    void deleteProduct_throws_when_productId_notExist() {
        // given
        Long notExistId = -1L;
        given(productDao.isNotExistById(notExistId)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> productService.deleteProduct(notExistId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("수정할 상품 ID가 존재하지 않으면 예외가 발생한다.")
    void updateProduct_throws_when_productId_notExist() {
        // given
        ProductRequest request = new ProductRequest(CHICKEN.NAME + "UPDATE", CHICKEN.PRICE + 10000, CHICKEN.IMAGE_URL);
        Long notExistId = -1L;
        given(productDao.isNotExistById(notExistId)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> productService.updateProduct(notExistId, request))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
    }
}
