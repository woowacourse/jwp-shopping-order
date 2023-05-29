package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import cart.dto.OrderPreviewResponse;
import cart.dto.OrderResponse;
import cart.dto.ProductInOrderResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderProductDao orderProductDao;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                orderDao,
                orderProductDao
        );
    }

    @Test
    void 주문_상세_조회_테스트() {
        //given
        final Member member = new Member("testMember", "password");
        final Order order = new Order(1L, null, member, 50_000, 45_000);
        final Product testProductA = new Product("testProductA", 10_000, "testImageA");
        final Product testProductB = new Product("testProductB", 20_000, "testImageB");
        final List<OrderProduct> orderProducts = List.of(new OrderProduct(order, testProductA, 3),
                new OrderProduct(order, testProductB, 1));
        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        given(orderProductDao.findByOrderId(anyLong())).willReturn(orderProducts);

        //when
        final OrderResponse orderResponse = orderService.findOrderById(member, order.getId());

        //then
        assertThat(orderResponse.getTotalPrice()).isEqualTo(50_000);
        assertThat(orderResponse.getProducts()).hasSize(2);
        final List<ProductInOrderResponse> products = orderResponse.getProducts();
        assertThat(products).extracting("count").contains(3, 1);
        assertThat(products).extracting("name").contains("testProductA", "testProductB");
    }

    @Test
    void 주문_목록_조회_테스트() {
        //given
        final Member member = new Member("testMember", "password");
        final Order orderA = new Order(1L, null, member, 30_000, 28_000);
        final Order orderB = new Order(2L, null, member, 90_000, 85_000);
        final Product testProductA = new Product("testProductA", 10_000, "testImageA");
        final Product testProductB = new Product("testProductB", 20_000, "testImageB");
        final List<OrderProduct> orderProducts = List.of(
                new OrderProduct(orderA, testProductA, 3),
                new OrderProduct(orderB, testProductA, 5),
                new OrderProduct(orderB, testProductB, 2)
        );
        given(orderProductDao.findByMemberId(any())).willReturn(orderProducts);

        //when
        final List<OrderPreviewResponse> allOrdersByMember = orderService.findAllOrdersByMember(member);

        //then
        assertThat(allOrdersByMember).hasSize(2);
        assertThat(allOrdersByMember).extracting("mainProductName").contains("testProductA");
        assertThat(allOrdersByMember).extracting("extraProductCount").contains(0, 1);
    }
}
