package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtils {

    public static Map<String, Integer> buildFrequencyMap(List<String> courses) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        courses.forEach(courseId -> {
            if (frequencyMap.containsKey(courseId)) {
                frequencyMap.put(courseId, frequencyMap.get(courseId) + 1);
            } else {
                frequencyMap.put(courseId, 1);
            }
        });

        return frequencyMap;
    }
}
