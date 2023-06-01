package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderDaoTest extends DaoTestHelper {

    @Test
    @DisplayName("주문 정보를 저장한다.")
    void insert() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, LocalDateTime.now());

        // when
        final Long 저장된_주문_아이디 = orderDao.insert(주문_엔티티);

        // then
        final List<OrderDto> 저장된_주문_정보 = orderDao.findById(저장된_주문_아이디);
        assertThat(저장된_주문_정보)
            .extracting(OrderDto::getOrderId, OrderDto::getTotalPrice, OrderDto::getDiscountedTotalPrice,
                OrderDto::getDeliveryPrice, OrderDto::getMemberId, OrderDto::getMemberName, OrderDto::getMemberPassword)
            .containsExactly(
                tuple(저장된_주문_아이디, 10000, 9000, 3000, 저장된_져니_아이디, "journey", "password")
            );
    }

    @Test
    @DisplayName("특정 회원이 주문한 전체 횟수를 구한다.")
    void countByMemberId() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, LocalDateTime.now());
        orderDao.insert(주문_엔티티);

        // when
        final Long 저장된_주문_횟수 = orderDao.countByMemberId(저장된_져니_아이디);

        // then
        assertThat(저장된_주문_횟수)
            .isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 아이디로 주문 정보를 조회한다.")
    void findById() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, 10000, 9000, 3000, LocalDateTime.now());
        final Long 저장된_주문_아이디 = orderDao.insert(주문_엔티티);

        // when
        final List<OrderDto> 저장된_주문_정보 = orderDao.findById(저장된_주문_아이디);

        // then
        assertThat(저장된_주문_정보)
            .extracting(OrderDto::getOrderId, OrderDto::getTotalPrice, OrderDto::getDiscountedTotalPrice,
                OrderDto::getDeliveryPrice, OrderDto::getMemberId, OrderDto::getMemberName, OrderDto::getMemberPassword)
            .containsExactly(
                tuple(저장된_주문_아이디, 10000, 9000, 3000, 저장된_져니_아이디, "journey", "password")
            );
    }
}
