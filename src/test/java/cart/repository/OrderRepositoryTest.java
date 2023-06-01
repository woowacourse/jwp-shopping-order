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
import cart.domain.order.OrderProduct;
import cart.repository.mapper.MemberMapper;
import cart.repository.mapper.OrderMapper;
import cart.repository.mapper.OrderProductMapper;
import cart.repository.mapper.ProductMapper;
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
    @DisplayName("findAllByMemberId 메서드는 멤버 ID에 해당하는 주문 정보 목록을 조회한다.")
    void findAllByMemberId() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
        Long memberId = memberDao.addMember(memberEntity);

        ProductEntity productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(productEntity);

        OrderEntity orderEntityA = new OrderEntity(memberEntity.assignId(memberId), 0, 0, 0);
        OrderEntity orderEntityB = new OrderEntity(memberEntity.assignId(memberId), 0, 0, 0);
        Long orderIdA = orderDao.save(orderEntityA);
        Long orderIdB = orderDao.save(orderEntityB);

        OrderProductEntity orderProductEntityA =
                new OrderProductEntity(orderIdA, productEntity.assignId(productId), "치킨", 10000, "http://chicken.com", 5);
        OrderProductEntity orderProductEntityB =
                new OrderProductEntity(orderIdB, productEntity.assignId(productId), "치킨", 10000, "http://chicken.com", 5);
        Long orderProductIdA = orderProductDao.save(orderProductEntityA);
        Long orderProductIdB = orderProductDao.save(orderProductEntityB);

        List<Order> result = orderRepository.findAllByMemberId(memberId);

        Order orderA = OrderMapper.toDomain(
                orderEntityA.assignId(orderIdA),
                List.of(orderProductEntityA.assignId(orderProductIdA))
        );
        Order orderB = OrderMapper.toDomain(
                orderEntityB.assignId(orderIdB),
                List.of(orderProductEntityB.assignId(orderProductIdB))
        );
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(orderA),
                () -> assertThat(result.get(1)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(orderB)
        );
    }

    @Test
    @DisplayName("findById 메서드는 ID에 해당하는 주문 정보를 조회한다.")
    void findById() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
        Long memberId = memberDao.addMember(memberEntity);

        ProductEntity productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(productEntity);

        OrderEntity orderEntity = new OrderEntity(memberEntity.assignId(memberId), 0, 0, 0);
        Long orderId = orderDao.save(orderEntity);

        OrderProductEntity orderProductEntity =
                new OrderProductEntity(orderId, productEntity.assignId(productId), "치킨", 10000, "http://chicken.com", 5);
        Long orderProductId = orderProductDao.save(orderProductEntity);

        Order result = orderRepository.findById(orderId);

        Order expected = OrderMapper.toDomain(
                orderEntity.assignId(orderId),
                List.of(orderProductEntity.assignId(orderProductId))
        );
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expected);
    }

    @Test
    @DisplayName("save 메서드는 주문을 저장한다.")
    void save() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
        Long memberId = memberDao.addMember(memberEntity);

        ProductEntity productEntity = new ProductEntity("치킨", 10000, "http://chicken.com");
        Long productId = productDao.createProduct(productEntity);

        OrderProduct orderProduct = new OrderProduct(ProductMapper.toDomain(productEntity.assignId(productId)), 10);
        Order order = new Order(MemberMapper.toDomain(memberEntity.assignId(memberId)), List.of(orderProduct), 1000);

        Long orderId = orderRepository.save(order);

        order.assignId(orderId);
        Order savedOrder = orderRepository.findById(orderId);
        List<OrderProductEntity> result = orderProductDao.findAllByOrderId(orderId);
        assertAll(
                () -> assertThat(savedOrder).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(order),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(OrderProductMapper.toDomain(result.get(0))).usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(orderProduct)
        );
    }
}
