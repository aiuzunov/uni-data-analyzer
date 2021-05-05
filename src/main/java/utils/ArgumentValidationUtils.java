package utils;

public class ArgumentValidationUtils {

    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("Value with name '%s' is null", name));
        }

        return value;
    }

    public static double requireNonNegative(double value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("Value with name '%s' is negative", name));
        }

        return value;
    }

    public static int requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("Value with name '%s' is negative", name));
        }

        return value;
    }

}