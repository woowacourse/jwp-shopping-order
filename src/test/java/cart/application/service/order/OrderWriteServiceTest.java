package cart.application.service.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.PointRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.domain.discountpolicy.PointPolicy;
import cart.ui.MemberAuth;
import cart.ui.order.CreateOrderDiscountDto;
import cart.ui.order.CreateOrderDto;
import cart.ui.order.CreateOrderItemDto;
import io.restassured.RestAssured;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
//@Sql("/data.sql")
@Sql("/reset.sql")
class OrderWriteServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    private OrderWriteService orderWriteService;

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
    private PointPolicy pointPolicy;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("주문을 올바르게 생성할 수 있다.")
    void createOrder() {
        MemberAuth memberAuth = new MemberAuth(5L, "레오", "leo@gmail.com", "leo123");
        List<CreateOrderItemDto> createOrderItemDtos = List.of(new CreateOrderItemDto(100L, 10L, 2),
                new CreateOrderItemDto(101L, 11L, 3)
        );
        CreateOrderDiscountDto createOrderDiscountDto = new CreateOrderDiscountDto(Collections.emptyList(), 1000);
        CreateOrderDto createOrderDto = new CreateOrderDto(createOrderItemDtos, createOrderDiscountDto);

        orderWriteService.createOrder(memberAuth, createOrderDto);
//        order 저장 ->
//        orderItem 저장 ->
//        member_coupon status 변경 ->
//        orderedCoupon 추가 ->
//        pointHistory 추가 ->
//        cartItem 삭제

    }

}
