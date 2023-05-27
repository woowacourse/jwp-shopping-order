package cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class DaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
}
