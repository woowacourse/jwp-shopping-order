package cart.dao;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.CouponFixture.할인율_쿠폰;
import static fixture.MemberFixture.유저_1;
import static fixture.OrderFixture.주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개;
import static fixture.OrderFixture.주문_유저_1_할인율_쿠폰_치킨_2개;
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
        OrderDto orderDto = new OrderDto(유저_1.getId(), 정액_할인_쿠폰.getId(), dateTime);

        Long orderId = orderDao.insert(orderDto);
        OrderDto orderDtoAfterSave = orderDao.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        assertThat(orderDtoAfterSave)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderDto);
    }

    @Test
    @DisplayName("orderDto를 조회하는 기능 테스트")
    void findByIdTest() {
        OrderDto orderDto = orderDao.findById(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(orderDto)
                .extracting(OrderDto::getId, OrderDto::getMemberId, OrderDto::getCouponId)
                .containsExactly(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId(), 유저_1.getId(), 정액_할인_쿠폰.getId());
    }

    /**
     * INSERT INTO orders(member_id, time_stamp, coupon_id) VALUES (1, '2023-01-01 12:00:00.000000', 1);
     * INSERT INTO orders(member_id, time_stamp, coupon_id) VALUES (1, '2023-01-01 12:00:00.000000', 2);
     */
    @Test
    @DisplayName("Member Id 로 Order 를 찾는다.")
    void findByMemberId() {
        List<OrderDto> orderDtos = orderDao.findByMemberId(유저_1.getId());

        assertThat(orderDtos)
                .extracting(OrderDto::getId, OrderDto::getMemberId, OrderDto::getCouponId)
                .containsExactly(
                        tuple(주문_유저_1_정액_할인_쿠폰_치킨_2개_샐러드_2개_피자_2개.getId(), 유저_1.getId(), 정액_할인_쿠폰.getId()),
                        tuple(주문_유저_1_할인율_쿠폰_치킨_2개.getId(), 유저_1.getId(), 할인율_쿠폰.getId()));
    }
}