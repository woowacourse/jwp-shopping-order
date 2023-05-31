package cart.application;

import static cart.TestSource.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.application.dto.GetOrdersRequest;
import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.dao.OrderDao;
import cart.dao.PointDao;
import cart.dao.ProductDao;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.Page;
import cart.domain.Paginator;
import cart.domain.Point;
import cart.domain.PointCalculator;
import cart.domain.Product;
import cart.exception.AuthenticationException;
import cart.exception.IllegalOrderException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {

    @Mock
    private Paginator<Order> paginator;
    @Mock
    private PointCalculator pointCalculator;
    @Mock
    private OrderDao orderDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private PointDao pointDao;
    @InjectMocks
    private OrderService orderService;

    @Test
    void 사용자의_주문_이력을_가져온다() {
        // given
        List<Order> orders = List.of(order1, order2, order3);
        Page<Order> pages = new Page<>(orders, 3, 1, 1);
        given(orderDao.findAllByMemberId(anyLong())).willReturn(orders);
        given(paginator.paginate(anyList(), anyInt())).willReturn(pages);

        // when & then
        assertDoesNotThrow(() -> orderService.getOrdersWithPagination(member1, new GetOrdersRequest(1)));
    }

    @Nested
    class 특정_주문을_상세_조회한다 {

        @Test
        void 다른_사용자의_주문을_조회하면_예외가_발생한다() {
            // given
            given(orderDao.findById(orderByMember1.getOrderId())).willReturn(orderByMember1);

            // when & then
            assertThatThrownBy(() -> orderService.getOrder(member2, orderByMember1.getOrderId()))
                .isInstanceOf(AuthenticationException.class);
        }

        @Test
        void 사용자의_특정_주문을_상세_조회한다() {
            // given
            given(orderDao.findById(orderByMember1.getOrderId())).willReturn(orderByMember1);

            // when & then
            assertDoesNotThrow(() -> orderService.getOrder(member1, orderByMember1.getOrderId()));
        }
    }

    @Nested
    class 주문을_추가한다 {

        @Test
        void 보유한_포인트보다_많은_포인트를_사용하면_예외가_발생한다() {
            // given
            Point currentPoint = new Point(1L, 1000, member1);
            PostOrderRequest request = new PostOrderRequest(currentPoint.getPoint() + 1,
                Collections.emptyList());
            given(pointDao.findByMemberId(member1.getId())).willReturn(currentPoint);

            // when & then
            assertThatThrownBy(() -> orderService.addOrder(member1, request))
                .isInstanceOf(IllegalOrderException.class)
                .hasMessage("보유한 포인트 이상을 사용할 수 없습니다");
        }

        @Test
        void 존재하지_않는_상품의_id로_주문을_요청하면_예외가_발생한다() {
            // given
            SingleKindProductRequest product1Request = new SingleKindProductRequest(1L, 1);
            SingleKindProductRequest product2Request = new SingleKindProductRequest(2L, 1);
            PostOrderRequest request = new PostOrderRequest(0, List.of(product1Request, product2Request));
            Point currentPoint = new Point(1L, 0, member1);
            Product foundProduct1 = new Product(1L, "", 0, "");

            given(pointDao.findByMemberId(member1.getId())).willReturn(currentPoint);
            given(productDao.getProductsByIds(anyList())).willReturn(List.of(foundProduct1));

            // when & then
            assertThatThrownBy(() -> orderService.addOrder(member1, request))
                .isInstanceOf(IllegalOrderException.class)
                .hasMessage("존재하지 않는 상품을 주문할 수 없습니다");
        }

        @Test
        void 지불할_금액을_초과하는_포인트를_사용하면_예외가_발생한다() {
            // given
            int pointOwned = 10000;
            SingleKindProductRequest product1Request = new SingleKindProductRequest(1L, 1);
            PostOrderRequest request = new PostOrderRequest(pointOwned, List.of(product1Request));
            Point currentPoint = new Point(1L, pointOwned, member1);
            Product foundProduct1 = new Product(1L, "", pointOwned - 1, "");

            given(pointDao.findByMemberId(member1.getId())).willReturn(currentPoint);
            given(productDao.getProductsByIds(anyList())).willReturn(List.of(foundProduct1));

            // when & then
            assertThatThrownBy(() -> orderService.addOrder(member1, request))
                .isInstanceOf(IllegalOrderException.class)
                .hasMessage("지불할 금액 이상으로 포인트를 사용할 수 없습니다");
        }

        @Test
        void 정상적인_주문이_추가된다() {
            Point currentPoint = new Point(1L, 1000, member1);
            SingleKindProductRequest product1Request = new SingleKindProductRequest(1L, 1);
            PostOrderRequest request = new PostOrderRequest(500, List.of(product1Request));
            Product foundProduct1 = new Product(1L, "", 10000, "");

            given(pointDao.findByMemberId(member1.getId())).willReturn(currentPoint);
            given(productDao.getProductsByIds(anyList())).willReturn(List.of(foundProduct1));
            given(pointCalculator.calculatePoint(anyInt())).willReturn(anyInt());

            assertDoesNotThrow(() -> orderService.addOrder(member1, request));
        }
    }

    @Nested
    class 주문을_취소한다 {

        @Test
        void 다른_사용자의_주문을_취소하려_하면_예외가_발생한다() {
            // given
            Order orderByMember2 = new Order(member2, LocalDateTime.now(), 0, 0, 0, OrderStatus.PENDING,
                Collections.emptyList());
            given(orderDao.findById(anyLong())).willReturn(orderByMember2);

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(member1, 1L))
                .isInstanceOf(IllegalOrderException.class)
                .hasMessage("올바르지 않은 요청입니다");
        }

        @Test
        void 결제_완료_상태가_아닌_주문을_취소하려_하면_예외가_발생한다() {
            // given
            Order orderByMember1 = new Order(member1, LocalDateTime.now(), 0, 0, 0, OrderStatus.PROCESSING,
                Collections.emptyList());
            given(orderDao.findById(anyLong())).willReturn(orderByMember1);

            // when & then
            assertThatThrownBy(() -> orderService.cancelOrder(member1, 1L))
                .isInstanceOf(IllegalOrderException.class)
                .hasMessage(OrderStatus.PENDING.getDisplayName() + " 상태인 주문만 취소가 가능합니다");
        }

        @Test
        void 정상적인_주문_취소가_이루어진다() {
            // given
            Order orderByMember1 = new Order(member1, LocalDateTime.now(), 100, 100, 5, OrderStatus.PENDING,
                Collections.emptyList());
            Point originalPoint = new Point(1L, 0, member1);
            given(orderDao.findById(anyLong())).willReturn(orderByMember1);
            given(pointDao.findByMemberId(anyLong())).willReturn(originalPoint);

            // when & then
            assertDoesNotThrow(() -> orderService.cancelOrder(member1, 1L));
        }
    }
}
