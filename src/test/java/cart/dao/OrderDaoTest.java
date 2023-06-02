package cart.dao;

import static fixture.MemberFixture.MEMBER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import anotation.RepositoryTest;
import cart.dao.dto.OrderDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("orderDto를 저장하는 기능 테스트")
    void insertTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        OrderDto orderDto = new OrderDto(2L, 1L, 1L, dateTime);

        Long orderId = orderDao.insert(orderDto);
        OrderDto orderDtoAfterSave = orderDao.findById(orderId).orElseThrow(NoSuchElementException::new);

        assertThat(orderDtoAfterSave)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderDto);
    }

    @Test
    @DisplayName("orderDto를 조회하는 기능 테스트")
    void findByIdTest() {
        OrderDto orderDto = orderDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(orderDto)
                .extracting(OrderDto::getId, OrderDto::getMemberId, OrderDto::getCouponId)
                .containsExactly(1L, 1L, 1L);
    }

    /**
     * INSERT INTO orders(member_id, time_stamp, coupon_id) VALUES (1, '2023-01-01 12:00:00.000000', 1);
     * INSERT INTO orders(member_id, time_stamp, coupon_id) VALUES (1, '2023-01-01 12:00:00.000000', 2);
     */
    @Test
    @DisplayName("Member Id 로 Order 를 찾는다.")
    void findByMemberId() {
        List<OrderDto> orderDtos = orderDao.findByMemberId(MEMBER_1.getId());

        assertThat(orderDtos)
                .extracting(OrderDto::getId, OrderDto::getMemberId, OrderDto::getCouponId)
                .containsExactly(tuple(1L, 1L, 1L), tuple(2L, 1L, 2L));
    }
}