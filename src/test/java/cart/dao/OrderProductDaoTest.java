package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderProductEntity;
import cart.dao.entity.ProductEntity;
import java.util.List;
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
class OrderProductDaoTest {

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
    private OrderProductDao orderProductDao;
    private ProductDao productDao;
    private MemberDao memberDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
        orderProductDao = new OrderProductDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    void 여러_개의_주문_상품을_저장한다() {
        // given
        ProductEntity ID가_있는_상품_엔티티 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(상품_엔티티);
        ProductEntity ID가_있는_두번째_상품_엔티티 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);

        MemberEntity 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Long 저장된_주문_ID = 주문을_저장하고_주문_ID를_리턴한다(ID가_있는_상품_엔티티, 멤버);

        OrderProductEntity 첫번째_주문_상품 = 저장할_주문_상품_생성(저장된_주문_ID, ID가_있는_상품_엔티티, 10);
        OrderProductEntity 두번째_주문_상품 = 저장할_주문_상품_생성(저장된_주문_ID, ID가_있는_두번째_상품_엔티티, 20);

        // when
        orderProductDao.saveAll(List.of(첫번째_주문_상품, 두번째_주문_상품));
        List<OrderProductEntity> 주문_상품들 = orderProductDao.findByOrderId(저장된_주문_ID);

        // then
        assertAll(
                () -> assertThat(주문_상품들).hasSize(2),
                () -> assertThat(주문_상품들).usingRecursiveComparison()
                        .ignoringFields("id", "updatedAt", "createdAt")
                        .isEqualTo(List.of(첫번째_주문_상품, 두번째_주문_상품))
        );
    }

    private Long 주문을_저장하고_주문_ID를_리턴한다(final ProductEntity ID가_있는_상품_엔티티, final MemberEntity 멤버) {
        int 사용한_포인트 = 10000;
        OrderEntity 주문 = new OrderEntity(null, 멤버.getId(), 사용한_포인트, ID가_있는_상품_엔티티.getPrice() / 10, 3000, null, null);
        return orderDao.save(주문);
    }

    private ProductEntity 상품을_저장하고_ID를_갖는_상품을_리턴한다(ProductEntity 상품_엔티티) {
        Long 저장된_상품_ID = productDao.save(상품_엔티티);
        return productDao.findById(저장된_상품_ID).get();
    }

    private MemberEntity 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 저장된_멤버_ID = memberDao.save(멤버_엔티티);
        return memberDao.findById(저장된_멤버_ID).get();
    }

    private OrderProductEntity 저장할_주문_상품_생성(Long 주문_ID, ProductEntity 상품_엔티티, int 수량) {
        return new OrderProductEntity(null, 주문_ID, 상품_엔티티, 수량);
    }
}
