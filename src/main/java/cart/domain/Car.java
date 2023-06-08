package cart.domain;

import org.springframework.stereotype.Component;

@Component
public class Car {
    private final String name;

    public Car(String name) {
        this.name = name;
    }

}
