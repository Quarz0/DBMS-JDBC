package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexEvaluator {

    public static String[] evaluate(String text, String pattern) {
        Pattern r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(text);

        if (m.find()) {
            String[] groups = new String[m.groupCount() + 1];
            for (int i = 0; i <= m.groupCount(); i++) {
                groups[i] = m.group(i);
            }
            return groups;
        } else {
            return null;
        }
    }

    private RegexEvaluator() {
    }

}
