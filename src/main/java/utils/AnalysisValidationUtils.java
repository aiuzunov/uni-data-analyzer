package utils;

import com.uni.data.analyzer.exceptions.MissingRequiredDataException;
import utils.data.MultiValueMap;

import java.util.Collection;
import java.util.Map;

public class AnalysisValidationUtils {

    private static final String MISSING_ANALYSIS_DATA_MESSAGE = "Липсват необходимите данни за '%s' анализ";

    public static <T> void requireNonEmpty(Collection<T> collection, String analysisName) {
        if (collection.size() == 0) {
            throw new MissingRequiredDataException(
                    String.format(MISSING_ANALYSIS_DATA_MESSAGE, analysisName));
        }
    }

    public static <K, V> void requireNonEmpty(Map<K, V> data, String analysisName) {
        if (data.size() == 0) {
            throw new MissingRequiredDataException(String.format(MISSING_ANALYSIS_DATA_MESSAGE, analysisName));
        }
    }

    public static <K, V1, V2> void requireNonEmpty(MultiValueMap<K, V1, V2> data, String analysisName) {
        if (data.size() == 0) {
            throw new MissingRequiredDataException(String.format(MISSING_ANALYSIS_DATA_MESSAGE, analysisName));
        }
    }

}