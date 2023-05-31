package cart.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.OrderWithOrderProductEntities;
import cart.dao.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderDaoTest {

    private final MemberEntity 멤버_엔티티 = new MemberEntity(
            null, "vero@email", "password", 20000, null, null
    );
    private final ProductEntity 상품_엔티티 = new ProductEntity(null,
            "치킨", 10_000, "http://example.com/chicken.jpg", null, null
    );

    private final ProductEntity 두번째_상품_엔티티 = new ProductEntity(null,
            "피자", 20_000, "http://example.com/pizza.jpg", null, null
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderDao orderDao;
    private ProductDao productDao;
    private MemberDao memberDao;
    private OrderProductDao orderProductDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        orderProductDao = new OrderProductDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void 주문_ID로_주문을_조회한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity ID가_있는_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        int 사용한_포인트 = 10000;
        OrderEntity 주문 = new OrderEntity(null, 멤버.getId(), 사용한_포인트, 3000, null, null);

        // when
        Long 저장된_주문_ID = orderDao.save(주문);
        orderProductDao.save(new OrderProductEntity(null, 저장된_주문_ID, ID가_있는_상품, 10));

        // then
        assertThat(orderDao.findById(저장된_주문_ID)).isPresent();
    }

    private MemberEntity 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 저장된_멤버_ID = memberDao.save(멤버_엔티티);
        return memberDao.findById(저장된_멤버_ID).get();
    }

    private ProductEntity 상품을_저장하고_ID를_갖는_상품을_리턴한다(ProductEntity 상품_엔티티) {
        Long 저장된_상품_ID = productDao.save(상품_엔티티);
        return productDao.findById(저장된_상품_ID).get();
    }

    @Test
    void 멤버_ID로_모든_주문을_조회한다() {
        // given
        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        ProductEntity 첫번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        OrderEntity 첫번째_주문 = new OrderEntity(null, 멤버.getId(), 10000, 3000, null, null);
        Long 저장된_첫번째_주문_ID = orderDao.save(첫번째_주문);
        orderProductDao.save(new OrderProductEntity(null, 저장된_첫번째_주문_ID, 첫번째_상품, 10));

        OrderEntity 두번째_주문 = new OrderEntity(null, 멤버.getId(), 10000, 3000, null, null);
        Long 저장된_두번째_주문_ID = orderDao.save(두번째_주문);
        ProductEntity 두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);
        orderProductDao.save(new OrderProductEntity(null, 저장된_두번째_주문_ID, 두번째_상품, 10));

        // when
        List<OrderWithOrderProductEntities> 주문들 = orderDao.findOrdersByMemberId(멤버.getId());

        List<Long> 주문_ID들 = 주문들.stream()
                .map(OrderWithOrderProductEntities::getOrderEntity)
                .map(OrderEntity::getId)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(주문들).hasSize(2),
                () -> assertThat(주문_ID들).contains(저장된_첫번째_주문_ID, 저장된_두번째_주문_ID)
        );
    }
}
