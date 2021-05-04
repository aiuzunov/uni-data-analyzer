package file.definitions;

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

    public String getName() {
        return name;
    }
}