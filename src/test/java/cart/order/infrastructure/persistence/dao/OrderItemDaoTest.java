package cart.order.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.common.DaoTest;
import cart.member.domain.Member;
import cart.member.infrastructure.persistence.dao.MemberDao;
import cart.order.infrastructure.persistence.entity.OrderEntity;
import cart.order.infrastructure.persistence.entity.OrderItemEntity;
import cart.product.domain.Product;
import cart.product.infrastructure.persistence.dao.ProductDao;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderItemDao 은(는)")
@DaoTest
class OrderItemDaoTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Test
    void 주문_상품들을_모두_저장한다() {
        // given
        memberDao.addMember(new Member(null, "email", "1234"));
        Member member = memberDao.getMemberByEmail("email");
        Long productId1 = productDao.createProduct(new Product("말랑", 100, "image"));
        Long productId2 = productDao.createProduct(new Product("코코닥", 300, "image"));
        Long orderId = 주문_저장(member.getId());
        OrderItemEntity orderItemEntity1 = new OrderItemEntity(null, 10, productId1, orderId);
        OrderItemEntity orderItemEntity2 = new OrderItemEntity(null, 20, productId2, orderId);

        // when
        List<OrderItemEntity> orderItemEntities = List.of(orderItemEntity1, orderItemEntity2);
        orderItemDao.saveAll(orderItemEntities);

        // then
        assertThat(orderItemDao.findAllByOrderId(orderId))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(orderItemEntities);
    }

    private Long 주문_저장(Long memberId) {
        OrderEntity orderEntity = new OrderEntity(null, memberId);
        return orderDao.save(orderEntity);
    }
}
