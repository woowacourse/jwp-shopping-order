package cart.persistence.dao;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:/init.sql")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class DaoTest {

}
