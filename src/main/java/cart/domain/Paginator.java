package cart.domain;

import static java.util.Comparator.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Paginator<T> {

    private final int dataPerPage;
    private final Function<T, Comparable> sortCriterion;
    private final boolean descending;

    public Paginator(int dataPerPage, Function<T, Comparable> sortCriterion) {
        this.dataPerPage = dataPerPage;
        this.sortCriterion = sortCriterion;
        this.descending = true;
    }

    public Page<T> paginate(List<T> contents, int page) {
        List<T> sorted = sort(contents);
        List<T> sliced = slice(sorted, page);
        int totalPage = getTotalPage(sorted);
        return new Page<>(sliced, dataPerPage, totalPage, page);
    }

    private List<T> sort(List<T> contents) {
        if (descending) {
            return (List<T>)contents.stream()
                .sorted(comparing(sortCriterion).reversed())
                .collect(Collectors.toList());
        }
        return (List<T>)contents.stream()
            .sorted(comparing(sortCriterion))
            .collect(Collectors.toList());
    }

    private List<T> slice(List<T> contents, int page) {
        if (contents.size() < page * dataPerPage) {
            return contents.subList((page - 1) * dataPerPage, contents.size());
        }
        return contents.subList((page - 1) * dataPerPage, page * dataPerPage);
    }

    private int getTotalPage(List<T> contents) {
        if (contents.size() % dataPerPage > 0) {
            return contents.size() / dataPerPage + 1;
        }
        return contents.size() / dataPerPage;
    }
}
