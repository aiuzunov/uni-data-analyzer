package file.definitions;

public enum StudentResultsFileColumns {
    ID("ID"),
    RESULT("Result");

    private final String name;

    StudentResultsFileColumns(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}