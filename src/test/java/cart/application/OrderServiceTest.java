package cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
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
    }
    
    
    @Test
    void findOrderByOrderId() {
    
    }
    
    @Test
    void deleteOrderById() {
    
    }
}
