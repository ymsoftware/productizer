package productizer.utils;

/**
 * Created by ymetelkin on 9/22/16.
 */
public class StringUtils {
    public static boolean isEmptyOrNull(final String s) {
        return s == null || s.trim().isEmpty();
    }
}
