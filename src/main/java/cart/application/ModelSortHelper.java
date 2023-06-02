package cart.application;

import cart.domain.Model;

import java.util.Collections;
import java.util.List;

public class ModelSortHelper {
    public static <E extends Model> void sortByIdInDescending(List<E> models) {
        Collections.sort(models, (a, b) -> (int) (b.getId() - a.getId()));
    }
}
