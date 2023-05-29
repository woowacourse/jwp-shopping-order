package cart.application;

import cart.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;


@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql({"/schema.sql", "/data.sql"})
@SpringBootTest
public class ServiceTest {

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected MemberDao memberDao;
}
