package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.CartItemEntity;
import cart.domain.Member;
import cart.domain.MemberEntity;
import cart.domain.Point;
import cart.domain.Product;
import cart.domain.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/schema.sql")
public class RepositoryTest {

    protected Point dummyPoint;
    protected Quantity dummyQuantity;
    protected Member dummyMember;
    protected MemberEntity dummyMemberEntity;
    protected Product dummyProduct;
    protected CartItem dummyCartItem;
    protected CartItemEntity dummyCartItemEntity;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected MemberDao memberDao;
    protected ProductDao productDao;
    protected CartItemDao cartItemDao;
    protected MemberRepository memberRepository;
    protected CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        this.dummyPoint = new Point(1);
        this.dummyQuantity = new Quantity(10);
        this.dummyMember = new Member(1L, "email", "pw", 1);
        this.dummyMemberEntity = new MemberEntity(dummyMember.getId(), dummyMember.getEmail(), dummyMember.getPassword(), dummyMember.getPointValue());
        this.dummyProduct = new Product(1L, "name", 1_000, "imageUrl", 10);
        this.dummyCartItem = new CartItem(1L, dummyMember, dummyProduct, dummyQuantity);
        this.dummyCartItemEntity = new CartItemEntity(dummyMemberEntity.getId(), dummyProduct.getId(), dummyQuantity.getValue());

        this.memberDao = new MemberDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.memberRepository = new MemberRepository(memberDao);
        this.cartItemRepository = new CartItemRepository(memberRepository, productDao, cartItemDao);
    }

    protected void insertDummyMemberAndProduct() {
        memberDao.insert(dummyMemberEntity);
        productDao.insert(dummyProduct);
    }
}
