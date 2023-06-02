package anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Sql(value = {"/drop-test.sql", "/schema-test.sql", "/data-test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest(includeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
@ActiveProfiles("test")
public @interface RepositoryTest {
}
