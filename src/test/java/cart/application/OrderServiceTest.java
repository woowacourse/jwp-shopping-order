package cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.dto.OrderAddRequest;
import cart.dto.OrderItemRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:initializeTestDb.sql")
class OrderServiceTest {
    
    @Autowired
    private OrderService orderService;
    
    //에러 테스트 추가 : 해당 product가 없는 경우
    //에러 테스트 추가 : product가 해당 장바구니에 없는 경우
    //에러 테스트 추가 : product의 수량이 장바구니의 수량과 일치하지 않는 경우
    @Test
    void addOrder() {
        Member member = new Member(1L, "abc@gmail.com", "12345");
        OrderAddRequest request = new OrderAddRequest(
                LocalDateTime.of(23, 05, 30, 13, 11, 30),
                List.of(new OrderItemRequest(1L, 2),
                        new OrderItemRequest(2L, 5)));
        
        assertThat(orderService.addOrder(member, request)).isEqualTo(1L);
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
                List.of(new OrderItem(1L, member.getId(), new Product(1l, "1번 상품", 1000, "1번 상품url"), 2),
                        new OrderItem(2l, member.getId(), new Product(2l, "2번 상품", 2000, "2번 상품url"), 5)),
                3000,
                0,
                createdAt
        );
        Order secondOrder = new Order(2L,
                member.getId(),
                List.of(new OrderItem(3L, member.getId(), new Product(3l, "3번 상품", 1000, "3번 상품url"), 3)),
                3000,
                0,
                createdAt
        );
        List<Order> expected = List.of(firstOrder, secondOrder);
        assertThat(orderService.findOrdersByMember(member))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
    
    
    @Test
    void findOrderByOrderId() {
    
    }
    
    @Test
    void deleteOrderById() {
    
    }
}
