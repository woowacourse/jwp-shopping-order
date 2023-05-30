package cart;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cart.domain.Order;
import cart.domain.Paginator;
import cart.domain.PointCalculator;

@Configuration
public class BeanConfig {

    @Bean
    public Paginator<Order> orderPaginator() {
        return new Paginator<>(10, Order::getOrderAt);
    }

    @Bean
    public PointCalculator pointCalculator() {
        return new PointCalculator();
    }
}
