package cart.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import cart.TestSource;
import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.dao.ProductDao;
import cart.domain.Product;
import cart.exception.IllegalOrderException;
import cart.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AddOrderServiceTest {

    @Mock
    ApplicationEventPublisher publisher;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductDao productDao;
    @InjectMocks
    AddOrderService addOrderService;

    @Test
    void 존재하지_않는_상품의_id가_요청되었을_경우_예외가_발생한다() {
        // productId = 1인 아이템만 존재하는데 2인 아이템에 대한 요청도 포함되었다.
        // given
        long productId1 = 1L;
        long productId2 = 2L;
        SingleKindProductRequest product1 = new SingleKindProductRequest(productId1, 10);
        SingleKindProductRequest product2 = new SingleKindProductRequest(productId2, 10);
        PostOrderRequest request = new PostOrderRequest(0, List.of(product1, product2));

        given(productDao.findProductsByIds(anyList())).willReturn(List.of(new Product(productId1, "", 0, "")));

        // when & then
        assertThatThrownBy(() -> addOrderService.addOrder(TestSource.member1, request))
            .isInstanceOf(IllegalOrderException.class)
            .hasMessage("존재하지 않는 상품을 주문할 수 없습니다");
    }

    @Test
    void 주문이_정상적으로_처리된다() {
        // given
        long productId1 = 1L;
        long productId2 = 2L;
        SingleKindProductRequest product1 = new SingleKindProductRequest(productId1, 10);
        SingleKindProductRequest product2 = new SingleKindProductRequest(productId2, 10);
        PostOrderRequest request = new PostOrderRequest(0, List.of(product1, product2));

        given(productDao.findProductsByIds(anyList()))
            .willReturn(List.of(
                new Product(productId1, "", 1000, ""),
                new Product(productId2, "", 2000, "")
            ));

        // when & then
        assertDoesNotThrow(() -> addOrderService.addOrder(TestSource.member1, request));
    }
}
