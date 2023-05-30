package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.order.Order;
import cart.repository.mapper.OrderProductMapper;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderProductDao orderProductDao;

    @Test
    @DisplayName("findById 메서드는 ID에 해당하는 주문 정보를 조회한다.")
    void findById() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
        Long memberId = memberDao.addMember(memberEntity);
        ProductEntity productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
        productDao.createProduct(productEntity);
        OrderEntity orderEntity = new OrderEntity(memberEntity.assignId(memberId), 0);
        Long orderId = orderDao.save(orderEntity);
        OrderProductEntity orderProductEntity =
                new OrderProductEntity(orderId, null, "치킨", 10000, "http://chicken.com", 5);
        Long orderProductId = orderProductDao.save(orderProductEntity);

        Order result = orderRepository.findById(orderId);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(orderId),
                () -> assertThat(result.getMember()).usingRecursiveComparison().isEqualTo(memberEntity.assignId(memberId)),
                () -> assertThat(result.getOrderProducts()).usingRecursiveComparison()
                        .isEqualTo(List.of(OrderProductMapper.toDomain(orderProductEntity.assignId(orderProductId)))),
                () -> assertThat(result.getUsedPoint()).isEqualTo(orderEntity.getUsedPoint()),
                () -> assertThat(result.getOrderedAt()).isNotNull()
        );
    }
}
