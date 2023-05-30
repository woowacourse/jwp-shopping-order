package cart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwpCartApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void DD(){
        double a = 0.1;
        int b = 14000;
        int c = 0;
        System.out.println((int) ((1-a) * b) + c);
    }
}
