package nl.sourcelabs.interview.duncan.assignment.util;

import java.util.ArrayList;
import java.util.List;

public interface Mapper <IN, OUT> {

    OUT map(IN input);

    default List<OUT> mapAll(Iterable<IN> iterable) {
        final List<OUT> result = new ArrayList<>();
        iterable.forEach(in -> result.add(map(in)));
        return result;
    }

}
