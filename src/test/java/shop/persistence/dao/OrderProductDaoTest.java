package shop.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.MemberEntity;
import shop.persistence.entity.OrderEntity;
import shop.persistence.entity.OrderProductEntity;
import shop.persistence.entity.ProductEntity;
import shop.persistence.entity.detail.OrderProductDetail;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({OrderProductDao.class, OrderDao.class, MemberDao.class, ProductDao.class})
class OrderProductDaoTest extends DaoTest {
    private static Long memberId;
    private static Long chickenId;
    private static Long pizzaId;
    private static Long orderId1;
    private static Long orderId2;
    private static OrderEntity orderEntity;

    @Autowired
    private OrderProductDao orderProductDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        memberId = memberDao.insertMember(Data.member);
        chickenId = productDao.insert(Data.chicken);
        pizzaId = productDao.insert(Data.pizza);

        orderEntity = new OrderEntity(memberId, 10000L,
                0L, 3000, LocalDateTime.now());
        orderId1 = orderDao.insert(orderEntity);
        orderId2 = orderDao.insert(orderEntity);
    }

    @DisplayName("주문 상품을 저장할 수 있다.")
    @Test
    void insertOrderProductTest() {
        //given
        OrderProductEntity orderChickenEntity
                = new OrderProductEntity(orderId1, chickenId, 20000, 10);

        //when
        orderProductDao.insert(orderChickenEntity);

        //then
        List<OrderProductDetail> orderProducts =
                orderProductDao.findAllByOrderId(orderId1);

        assertThat(orderProducts.size()).isEqualTo(1);
        assertThat(orderProducts).extractingResultOf("getProductName")
                .containsExactly(Data.chicken.getName());
        assertThat(orderProducts).extractingResultOf("getOrderedProductPrice")
                .containsExactly(20000);
        assertThat(orderProducts).extractingResultOf("getQuantity")
                .containsExactly(10);
        assertThat(orderProducts).extractingResultOf("getOrderId")
                .containsExactly(orderId1);
    }


    @DisplayName("주문의 상품들을 조회할 수 있다.")
    @Test
    void findAllByOrderIdTest() {
        //given
        OrderProductEntity orderChickenEntity
                = new OrderProductEntity(orderId1, chickenId, 20000, 10);
        OrderProductEntity orderPizzaEntity
                = new OrderProductEntity(orderId1, pizzaId, 30000, 5);

        //when
        orderProductDao.insertAll(List.of(orderChickenEntity, orderPizzaEntity));

        //then
        List<OrderProductDetail> orderProductsOfOrderId = orderProductDao.findAllByOrderId(orderId1);

        assertThat(orderProductsOfOrderId.size()).isEqualTo(2);
        assertThat(orderProductsOfOrderId).extractingResultOf("getProductName")
                .containsExactlyInAnyOrder(Data.pizza.getName(), Data.chicken.getName());
        assertThat(orderProductsOfOrderId).extractingResultOf("getOrderedProductPrice")
                .containsExactlyInAnyOrder(20000, 30000);
        assertThat(orderProductsOfOrderId).extractingResultOf("getQuantity")
                .containsExactlyInAnyOrder(10, 5);
    }

    @DisplayName("여러 주문의 상품들을 조회할 수 있다.")
    @Test
    void findAllByOrderIdsTest() {
        //given
        OrderProductEntity orderChickenEntity
                = new OrderProductEntity(orderId1, chickenId, 20000, 10);
        OrderProductEntity orderPizzaEntity
                = new OrderProductEntity(orderId2, pizzaId, 30000, 10);

        //when
        orderProductDao.insert(orderChickenEntity);
        orderProductDao.insert(orderPizzaEntity);

        //then
        List<OrderProductDetail> orderProducts =
                orderProductDao.findAllByOrderIds(List.of(orderId1, orderId2));

        assertThat(orderProducts.size()).isEqualTo(2);
        assertThat(orderProducts).extractingResultOf("getProductName")
                .containsExactlyInAnyOrder(Data.pizza.getName(), Data.chicken.getName());
        assertThat(orderProducts).extractingResultOf("getOrderedProductPrice")
                .containsExactlyInAnyOrder(20000, 30000);
        assertThat(orderProducts).extractingResultOf("getQuantity")
                .containsExactlyInAnyOrder(10, 10);
        assertThat(orderProducts).extractingResultOf("getOrderId")
                .containsExactlyInAnyOrder(orderId1, orderId2);
    }

    private static class Data {
        static final MemberEntity member = new MemberEntity("쥬니", "1234");
        static final ProductEntity pizza = new ProductEntity("피자", 20000, "피자.com");
        static final ProductEntity chicken = new ProductEntity("치킨", 30000, "치킨.com");
    }
}
