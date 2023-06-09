package cart.persistence.dao;

import cart.persistence.entity.CartItemEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import({MemberDao.class, ProductDao.class})
public class CartItemDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private CartItemDao cartItemDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    public CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingColumns("quantity", "product_id", "member_id")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 아이디에_포함되는_모든_장바구니_상품을_삭제한다() {
        MemberEntity memberEntity = new MemberEntity(null, "cartItemMember@a.com", "1234");
        long memberId = memberDao.add(memberEntity);
        ProductEntity productEntity = new ProductEntity(null, "product", 10000, "url");
        long productId = productDao.add(productEntity);
        CartItemEntity cartItemEntity = new CartItemEntity(null, 1, productId, memberId);
        CartItemEntity cartItemEntity2 = new CartItemEntity(null, 2, productId, memberId);
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(cartItemEntity);
        long cartItemId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        SqlParameterSource sqlParameterSource2 = new BeanPropertySqlParameterSource(cartItemEntity2);
        long cartItemId2 = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource2).longValue();

        List<Long> cartItemIds = List.of(cartItemId, cartItemId2);
        cartItemDao.deleteByIds(cartItemIds);

        Optional<CartItemEntity> cartItem = cartItemDao.findById(cartItemId);
        Optional<CartItemEntity> cartItem2 = cartItemDao.findById(cartItemId2);

        assertThat(cartItem).isEqualTo(Optional.empty());
        assertThat(cartItem2).isEqualTo(Optional.empty());
    }
}
