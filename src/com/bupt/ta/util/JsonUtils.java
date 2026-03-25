package com.bupt.ta.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 轻量级 JSON 读写工具。
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    public static Object parse(String json) {
        if (json == null) {
            return null;
        }
        Parser parser = new Parser(json);
        Object value = parser.parseValue();
        parser.skipWhitespace();
        if (!parser.isEnd()) {
            throw new IllegalArgumentException("Invalid JSON content");
        }
        return value;
    }

    public static String toJson(Object value) {
        StringBuilder builder = new StringBuilder();
        writeValue(builder, value);
        return builder.toString();
    }

    private static void writeValue(StringBuilder builder, Object value) {
        if (value == null) {
            builder.append("null");
            return;
        }

        if (value instanceof String) {
            builder.append('"').append(escape(String.valueOf(value))).append('"');
            return;
        }

        if (value instanceof Number || value instanceof Boolean) {
            builder.append(String.valueOf(value));
            return;
        }

        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            builder.append('{');
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.append('"').append(escape(entry.getKey())).append('"').append(':');
                writeValue(builder, entry.getValue());
                if (iterator.hasNext()) {
                    builder.append(',');
                }
            }
            builder.append('}');
            return;
        }

        if (value instanceof Iterable) {
            builder.append('[');
            Iterator<?> iterator = ((Iterable<?>) value).iterator();
            while (iterator.hasNext()) {
                writeValue(builder, iterator.next());
                if (iterator.hasNext()) {
                    builder.append(',');
                }
            }
            builder.append(']');
            return;
        }

        builder.append('"').append(escape(String.valueOf(value))).append('"');
    }

    private static String escape(String raw) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char ch = raw.charAt(i);
            switch (ch) {
                case '"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                default:
                    if (ch < 32) {
                        builder.append(String.format("\\u%04x", (int) ch));
                    } else {
                        builder.append(ch);
                    }
            }
        }
        return builder.toString();
    }

    private static final class Parser {
        private final String text;
        private int index;

        private Parser(String text) {
            this.text = text;
        }

        private Object parseValue() {
            skipWhitespace();
            if (isEnd()) {
                return null;
            }

            char current = currentChar();
            if (current == '{') {
                return parseObject();
            }
            if (current == '[') {
                return parseArray();
            }
            if (current == '"') {
                return parseString();
            }
            if (current == 't') {
                expect("true");
                return Boolean.TRUE;
            }
            if (current == 'f') {
                expect("false");
                return Boolean.FALSE;
            }
            if (current == 'n') {
                expect("null");
                return null;
            }
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            expect('{');
            skipWhitespace();
            if (peek('}')) {
                expect('}');
                return map;
            }

            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                map.put(key, value);
                skipWhitespace();
                if (peek('}')) {
                    expect('}');
                    break;
                }
                expect(',');
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<Object>();
            expect('[');
            skipWhitespace();
            if (peek(']')) {
                expect(']');
                return list;
            }

            while (true) {
                list.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    expect(']');
                    break;
                }
                expect(',');
            }
            return list;
        }

        private String parseString() {
            expect('"');
            StringBuilder builder = new StringBuilder();
            while (!isEnd()) {
                char current = currentChar();
                index++;
                if (current == '"') {
                    break;
                }
                if (current == '\\') {
                    if (isEnd()) {
                        throw new IllegalArgumentException("Invalid escape sequence");
                    }
                    char escaped = currentChar();
                    index++;
                    switch (escaped) {
                        case '"':
                            builder.append('"');
                            break;
                        case '\\':
                            builder.append('\\');
                            break;
                        case '/':
                            builder.append('/');
                            break;
                        case 'b':
                            builder.append('\b');
                            break;
                        case 'f':
                            builder.append('\f');
                            break;
                        case 'n':
                            builder.append('\n');
                            break;
                        case 'r':
                            builder.append('\r');
                            break;
                        case 't':
                            builder.append('\t');
                            break;
                        case 'u':
                            builder.append(parseUnicode());
                            break;
                        default:
                            throw new IllegalArgumentException("Unsupported escape sequence");
                    }
                } else {
                    builder.append(current);
                }
            }
            return builder.toString();
        }

        private char parseUnicode() {
            if (index + 4 > text.length()) {
                throw new IllegalArgumentException("Invalid unicode escape");
            }
            String hex = text.substring(index, index + 4);
            index += 4;
            return (char) Integer.parseInt(hex, 16);
        }

        private Number parseNumber() {
            int start = index;
            if (peek('-')) {
                index++;
            }
            while (!isEnd() && Character.isDigit(currentChar())) {
                index++;
            }
            if (!isEnd() && currentChar() == '.') {
                index++;
                while (!isEnd() && Character.isDigit(currentChar())) {
                    index++;
                }
            }
            if (!isEnd() && (currentChar() == 'e' || currentChar() == 'E')) {
                index++;
                if (!isEnd() && (currentChar() == '+' || currentChar() == '-')) {
                    index++;
                }
                while (!isEnd() && Character.isDigit(currentChar())) {
                    index++;
                }
            }

            String number = text.substring(start, index);
            if (number.contains(".") || number.contains("e") || number.contains("E")) {
                return Double.valueOf(number);
            }
            return Long.valueOf(number);
        }

        private void expect(String expected) {
            if (!text.startsWith(expected, index)) {
                throw new IllegalArgumentException("Invalid JSON token");
            }
            index += expected.length();
        }

        private void expect(char expected) {
            if (isEnd() || currentChar() != expected) {
                throw new IllegalArgumentException("Invalid JSON structure");
            }
            index++;
        }

        private boolean peek(char expected) {
            return !isEnd() && currentChar() == expected;
        }

        private char currentChar() {
            return text.charAt(index);
        }

        private void skipWhitespace() {
            while (!isEnd() && Character.isWhitespace(currentChar())) {
                index++;
            }
        }

        private boolean isEnd() {
            return index >= text.length();
        }
    }
}
