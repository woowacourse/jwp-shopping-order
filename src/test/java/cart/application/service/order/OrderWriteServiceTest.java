package cart.application.service.order;

import cart.application.repository.coupon.CouponRepository;
import cart.application.repository.point.PointRepository;
import cart.application.repository.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderWriteServiceTest {

    @Autowired
    private OrderWriteService orderWriteService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Test
    void createOrder() {
//        orderWriteService.createOrder()
    }
}
