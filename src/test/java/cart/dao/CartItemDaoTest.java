package cart.dao;

import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import static cart.fixture.MemberFixture.주노;
import static cart.fixture.MemberFixture.헤나;
import static cart.fixture.ProductFixture.치킨;
import static cart.fixture.ProductFixture.핫도그;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CartItemDaoTest {

    private static final RowMapper<CartItemEntity> ROW_MAPPER = (rs, rowNum) -> new CartItemEntity(
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert cartItemSimpleInsert;
    private SimpleJdbcInsert productSimpleInsert;
    private SimpleJdbcInsert memberSimpleInsert;
    private CartItemDao cartItemDao;

    @BeforeEach
    void setUp() {
        cartItemSimpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");

        productSimpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");

        memberSimpleInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");

        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @Test
    void 장바구니를_저장한다() {
        long 상품_식별자 = 상품_저장(치킨.ENTITY);
        long 유저_식별자 = 유저_저장(주노.ENTITY);

        CartItemEntity 장바구니 = new CartItemEntity(상품_식별자, 유저_식별자, 1);
        Long 장바구니_식별자 = cartItemDao.insert(장바구니);

        String sql = "SELECT * FROM cart_item WHERE id = ?";
        CartItemEntity result = jdbcTemplate.queryForObject(sql, ROW_MAPPER, 장바구니_식별자);

        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(장바구니);
    }

    @Test
    void 장바구니를_갱신한다() {
        long 상품_식별자 = 상품_저장(핫도그.ENTITY);
        long 유저_식별자 = 유저_저장(헤나.ENTITY);
        CartItemEntity 장바구니 = new CartItemEntity(상품_식별자, 유저_식별자, 1);

        SqlParameterSource source = new BeanPropertySqlParameterSource(장바구니);
        long 장바구니_식별자 = cartItemSimpleInsert.executeAndReturnKey(source).longValue();

        int 개수_수정 = 10;
        cartItemDao.update(장바구니_식별자, 개수_수정);

        String sql = "SELECT * FROM cart_item WHERE id = ?";
        CartItemEntity result = jdbcTemplate.queryForObject(sql, ROW_MAPPER, 장바구니_식별자);

        Assertions.assertThat(result.getQuantity()).isEqualTo(개수_수정);
    }

    @Test
    void 식별자_값으로_장바구니를_삭제한다() {
        long 상품_식별자 = 상품_저장(치킨.ENTITY);
        long 유저_식별자 = 유저_저장(주노.ENTITY);
        CartItemEntity 장바구니 = new CartItemEntity(상품_식별자, 유저_식별자, 1);

        SqlParameterSource source = new BeanPropertySqlParameterSource(장바구니);
        long 장바구니_식별자 = cartItemSimpleInsert.executeAndReturnKey(source).longValue();

        cartItemDao.deleteById(장바구니_식별자);

        String sql = "SELECT * FROM cart_item WHERE id = ?";
        Assertions.assertThatThrownBy(() -> jdbcTemplate.queryForObject(sql, ROW_MAPPER, 장바구니_식별자))
                .isInstanceOf(DataAccessException.class);
    }

    private long 상품_저장(ProductEntity productEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(productEntity);
        return productSimpleInsert.executeAndReturnKey(source).longValue();
    }

    private long 유저_저장(MemberEntity memberEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(memberEntity);
        return memberSimpleInsert.executeAndReturnKey(source).longValue();
    }
}
