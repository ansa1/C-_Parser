public class Utils {
    // constant string to remove BOM from the file
    public static final String UTF8_BOM = "\uFEFF";

    public static final String WINDOWS_LINE_SEPARATOR = "\r\n";
    public static final String MODERN_UNIX_LINE_SEPARATOR = "\n";

    // using this type of line delimiter is deprecated and should be avoided
    public static final String OLD_UNIX_LINE_SEPARATOR = "\r";

    // removing BOM method
    public static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    public static String getInputLineSeparator(String input) {
        if (input.contains(WINDOWS_LINE_SEPARATOR)) {
            // Windows case
            return WINDOWS_LINE_SEPARATOR;
        } else if (input.contains(MODERN_UNIX_LINE_SEPARATOR)) {
            // Modern UNIX case
            return MODERN_UNIX_LINE_SEPARATOR;
        } else {
            // Old UNIX case (deprecated)
            return OLD_UNIX_LINE_SEPARATOR;
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
