package cart.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Products {

  private final List<Product> values;

  public Products(List<Product> values) {
    this.values = values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Products products = (Products) o;
    return Objects.equals(getValues(), products.getValues());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValues());
  }

  public List<Product> getValues() {
    return values;
  }
}
