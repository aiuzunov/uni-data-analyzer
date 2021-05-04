package file.definitions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public enum LogFileColumns {
    TIME("Time"),
    EVENT_CONTEXT("Event context"),
    COMPONENT("Component"),
    EVENT_NAME("Event name"),
    DESCRIPTION("Description");

    private final String name;

    LogFileColumns(String name) {
        this.name = name;
    }

    public static List<String> getUTF8Values() {
        List<String> values = Arrays.stream(values()).map(LogFileColumns::getName)
                .collect(Collectors.toList());
        values.replaceAll(column -> column.equals("Time") ?
                "\uFEFFTime" :
                column);

        return unmodifiableList(values);
    }

    public String getName() {
        return name;
    }
}