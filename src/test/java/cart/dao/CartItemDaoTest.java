package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class CartItemDaoTest {

    @Autowired
    DataSource dataSource;

    CartItemDao cartItemDao;

    @BeforeEach
    void setting() {
        cartItemDao = new CartItemDao(new JdbcTemplate(dataSource));
    }

    @DisplayName("카트 아이템 삭제 테스트")
    @Test
    void delete() {
        //given
        final Long cartItem1 = cartItemDao.save(new CartItem(new Member(1l, "email1", "password1"), new Product(1L, "product1", 1000, "image1")));
        final Long cartItem2 = cartItemDao.save(new CartItem(new Member(2l, "email2", "password2"), new Product(2L, "product1", 1000, "image1")));

        assertThat(cartItemDao.findById(cartItem1)).isPresent();
        assertThat(cartItemDao.findById(cartItem2)).isPresent();

        //when
        cartItemDao.deleteByIds(List.of(cartItem1, cartItem2));

        //then
        assertAll(
                () -> assertThat(cartItemDao.findById(cartItem1)).isNotPresent(),
                () -> assertThat(cartItemDao.findById(cartItem1)).isNotPresent()
        );
    }

}
