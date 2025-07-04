package dynastxu.fussy.utils;

import dynastxu.fussy.Config;

import java.util.HashMap;
import java.util.Map;

public class FussyUtils {
    private static final float DEFAULT_PREFERENCE_PERCENT = Config.DEFAULT_PREFERENCE_PERCENT.get() / 100f;

    public static Map<String, Float> cleanUp(Map<String, Float> foodPreferences){
        Map<String, Float> foodPreferenceClean = new HashMap<>();
        for (Map.Entry<String, Float> entry : foodPreferences.entrySet()) {
            if (entry.getValue() != DEFAULT_PREFERENCE_PERCENT) {
                foodPreferenceClean.put(entry.getKey(), entry.getValue());
            }
        }
        return foodPreferenceClean;
    }
}
