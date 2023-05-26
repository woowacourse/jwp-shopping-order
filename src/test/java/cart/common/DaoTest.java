package cart.common;

import cart.common.annotation.Dao;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JdbcTest(
        includeFilters = {
                @Filter(type = FilterType.ANNOTATION, classes = Dao.class)
        }
)
@Sql("/truncate.sql")
public @interface DaoTest {
}
