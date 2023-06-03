package cart.application.service.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.PointRepository;
import cart.application.repository.ProductRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.ui.MemberAuth;
import cart.ui.order.CreateOrderItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("/reset.sql")
class OrderWriteServiceTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderedItemRepository orderedItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderWriteService orderWriteService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void createOrder() {
        MemberAuth memberAuth = new MemberAuth(10L, "leo", "leo@gmail.com", "qwer1234");
//        new;
        new CreateOrderItemDto(1L, 1L, 3);
//        orderWriteService.createOrder();
    }
}
