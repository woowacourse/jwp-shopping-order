package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Products {

  private final List<Product> values;

  public Products(List<Product> values) {
    this.values = values;
  }

  public List<Product> getValues() {
    return values;
  }
}
