package shop.persistence.dao;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@JdbcTest
public class DaoTest {
}
