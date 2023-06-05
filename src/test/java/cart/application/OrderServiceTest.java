package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.ProductDao;
import cart.domain.BasicDeliveryFeeCalculator;
import cart.domain.BasicDiscountCalculator;
import cart.domain.DeliveryFeeCalculator;
import cart.domain.DiscountCalculator;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.OrderItems;
import cart.domain.Product;
import cart.dto.OrderAddRequest;
import cart.dto.OrderItemRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:initializeTestDb.sql")
class OrderServiceTest {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private DeliveryFeeCalculator deliveryFeeCalculator;
    private DiscountCalculator discountCalculator;
    private CartItemDao cartItemDao;
    private ProductDao productDao;
    private OrderItemDao orderItemDao;
    private OrderDao orderDao;
    private OrderService orderService;
    
    @BeforeEach
    void setup() {
        deliveryFeeCalculator = new BasicDeliveryFeeCalculator();
        discountCalculator = new BasicDiscountCalculator();
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
        orderService = new OrderService(deliveryFeeCalculator, discountCalculator, cartItemDao, productDao,
                orderItemDao, orderDao);
    }
    
    //에러 테스트 추가 : 해당 product가 없는 경우
    //에러 테스트 추가 : product가 해당 장바구니에 없는 경우
    //에러 테스트 추가 : product의 수량이 장바구니의 수량과 일치하지 않는 경우
    @Test
    void addOrder() {
        Member member = new Member(1L, "abc@gmail.com", "12345");
        LocalDateTime createdAt = LocalDateTime.of(23, 05, 30, 13, 11, 30);
        OrderAddRequest request = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 5)));
        Order expected = new Order(1L,
                member.getId(),
                new OrderItems(List.of(new OrderItem(1L, new Product(1l, "1번 상품", 1000, "1번 상품url"), 2),
                        new OrderItem(2l, new Product(2l, "2번 상품", 2000, "2번 상품url"), 5))),
                3000,
                0,
                createdAt
        );
        
        assertThat(orderService.addOrder(member, request)).isEqualTo(expected);
    }
    
    
    @Test
    void findOrdersByMemberId() {
        //given
        Member member = new Member(1L, "abc@gmail.com", "12345");
        LocalDateTime createdAt = LocalDateTime.of(23, 05, 30, 13, 11, 30);
        OrderAddRequest firstRequest = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 5)));
        OrderAddRequest secondRequest = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(3L, 3)));
        orderService.addOrder(member, firstRequest);
        orderService.addOrder(member, secondRequest);
        
        //expected
        Order firstOrder = new Order(1L,
                member.getId(),
                new OrderItems(List.of(new OrderItem(1L, new Product(1l, "1번 상품", 1000, "1번 상품url"), 2),
                        new OrderItem(2l, new Product(2l, "2번 상품", 2000, "2번 상품url"), 5))),
                3000,
                0,
                createdAt
        );
        Order secondOrder = new Order(2L,
                member.getId(),
                new OrderItems(List.of(new OrderItem(3L, new Product(3l, "3번 상품", 1000, "3번 상품url"), 3))),
                3000,
                0,
                createdAt
        );
        List<Order> expected = List.of(firstOrder, secondOrder);
        
        assertThat(orderService.findOrdersByMember(member))
                .isEqualTo(expected);
    }
    
    
    @Test
    void findOrderByOrderId() {
        //given
        Member member = new Member(1L, "abc@gmail.com", "12345");
        LocalDateTime createdAt = LocalDateTime.of(23, 05, 30, 13, 11, 30);
        OrderAddRequest firstRequest = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 5)));
        OrderAddRequest secondRequest = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(3L, 3)));
        orderService.addOrder(member, firstRequest);
        orderService.addOrder(member, secondRequest);
    
        Order secondOrder = new Order(2L,
                member.getId(),
                new OrderItems(List.of(new OrderItem(3L, new Product(3l, "3번 상품", 1000, "3번 상품url"), 3))),
                3000,
                0,
                createdAt
        );
    
        assertThat(orderService.findOrderById(member, 2L))
                .isEqualTo(secondOrder);
    }
    
    @Test
    void deleteOrderById() {
        //given
        Member member = new Member(1L, "abc@gmail.com", "12345");
        LocalDateTime createdAt = LocalDateTime.of(23, 05, 30, 13, 11, 30);
        OrderAddRequest firstRequest = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 5)));
        OrderAddRequest secondRequest = new OrderAddRequest(
                createdAt,
                List.of(new OrderItemRequest(3L, 3)));
        orderService.addOrder(member, firstRequest);
        orderService.addOrder(member, secondRequest);
    
        //expected
        Order removedOrder = new Order(1L,
                member.getId(),
                new OrderItems(List.of(new OrderItem(1L, new Product(1l, "1번 상품", 1000, "1번 상품url"), 2),
                        new OrderItem(2l, new Product(2l, "2번 상품", 2000, "2번 상품url"), 5))),
                3000,
                0,
                createdAt
        );
        assertThat(orderService.findOrdersByMember(member)).contains(removedOrder);
        
        //then
        assertDoesNotThrow(()->orderService.deleteOrder(1L));
        assertThat(orderService.findOrdersByMember(member)).doesNotContain(removedOrder);
    }
}
