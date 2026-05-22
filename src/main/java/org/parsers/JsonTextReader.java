package org.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class JsonTextReader {
    private JsonTextReader() {
    }

    static String findString(String json, String key) {
        Matcher matcher = Pattern
                .compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\\\\\"])*)\\\"", Pattern.DOTALL)
                .matcher(json);
        return matcher.find() ? unescape(matcher.group(1)) : null;
    }

    static Long findLong(String json, String key) {
        Matcher matcher = Pattern
                .compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*(-?\\d+)", Pattern.DOTALL)
                .matcher(json);
        return matcher.find() ? Long.parseLong(matcher.group(1)) : null;
    }

    static String findObject(String json, String key) {
        return findEnclosedValue(json, key, '{', '}');
    }

    static String findArray(String json, String key) {
        return findEnclosedValue(json, key, '[', ']');
    }

    static List<String> splitObjects(String arrayText) {
        List<String> result = new ArrayList<>();
        if (arrayText == null || arrayText.isBlank()) {
            return result;
        }
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;
        int start = -1;
        for (int i = 0; i < arrayText.length(); i++) {
            char ch = arrayText.charAt(i);
            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (ch == '\\') {
                    escaped = true;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }
            if (ch == '"') {
                inString = true;
            } else if (ch == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    result.add(arrayText.substring(start, i + 1));
                    start = -1;
                }
            }
        }
        return result;
    }

    private static String findEnclosedValue(String json, String key, char open, char close) {
        Matcher matcher = Pattern
                .compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:", Pattern.DOTALL)
                .matcher(json);
        if (!matcher.find()) {
            return null;
        }
        int start = json.indexOf(open, matcher.end());
        if (start < 0) {
            return null;
        }
        int end = findMatchingBracket(json, start, open, close);
        return end < 0 ? null : json.substring(start, end + 1);
    }

    private static int findMatchingBracket(String text, int start, char open, char close) {
        int depth = 0;
        boolean inString = false;
        boolean escaped = false;
        for (int i = start; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (ch == '\\') {
                    escaped = true;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }
            if (ch == '"') {
                inString = true;
            } else if (ch == open) {
                depth++;
            } else if (ch == close) {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static String unescape(String text) {
        return text
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}
