package cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class JwpCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwpCartApplication.class, args);
    }
}
