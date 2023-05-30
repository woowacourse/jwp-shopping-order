package cart;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cart.domain.Order;
import cart.domain.Paginator;

@Configuration
public class BeanConfig {

    @Bean
    public Paginator<Order> orderPaginator() {
        return new Paginator<>(10, Order::getOrderAt);
    }
}
