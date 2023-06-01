package cart.dao;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import({CartItemDao.class, MemberDao.class, ProductDao.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DaoTest {

    @Autowired
    CartItemDao cartItemDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    ProductDao productDao;
}