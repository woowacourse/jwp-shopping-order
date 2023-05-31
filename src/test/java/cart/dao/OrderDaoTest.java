package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("findAllByMemberId 메서드는 멤버의 주문 정보 목록을 반환한다.")
    void findAllByMemberId() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
        Long memberId = memberDao.addMember(memberEntity);

        OrderEntity orderEntityA = new OrderEntity(memberEntity.assignId(memberId), 0, 0);
        OrderEntity orderEntityB = new OrderEntity(memberEntity.assignId(memberId), 0, 0);
        Long orderIdA = orderDao.save(orderEntityA);
        Long orderIdB = orderDao.save(orderEntityB);

        List<OrderEntity> result = orderDao.findAllByMemberId(memberId);

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(orderEntityA.assignId(orderIdA)),
                () -> assertThat(result.get(1)).usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(orderEntityB.assignId(orderIdB))
        );
    }

    @Nested
    @DisplayName("findById 메서드는 ")
    class FindById {

        @Test
        @DisplayName("ID에 해당하는 주문이 존재하지 않으면 빈 값을 반환한다.")
        void emptyOrder() {
            Optional<OrderEntity> result = orderDao.findById(-1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("ID에 해당하는 주문이 존재하면 주문 정보를 반환한다.")
        void findOrder() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
            Long memberId = memberDao.addMember(memberEntity);

            OrderEntity orderEntity = new OrderEntity(memberEntity.assignId(memberId), 0, 0);
            Long orderId = orderDao.save(orderEntity);

            Optional<OrderEntity> result = orderDao.findById(orderId);
            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison()
                            .ignoringExpectedNullFields()
                            .isEqualTo(orderEntity.assignId(orderId))
            );
        }
    }
}
