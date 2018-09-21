import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

class LexicalAnalyzer {
    String[] keywords = {"abstract", "as", "base", "bool", "break", "by",
            "byte", "case", "catch", "char", "checked", "class", "const",
            "continue", "decimal", "default", "delegate", "do", "double",
            "descending", "explicit", "event", "extern", "else", "enum",
            "false", "finally", "fixed", "float", "for", "foreach", "from",
            "goto", "group", "if", "implicit", "in", "int", "interface",
            "internal", "into", "is", "lock", "long", "new", "null", "namespace",
            "object", "operator", "out", "override", "orderby", "params",
            "private", "protected", "public", "readonly", "ref", "return",
            "switch", "struct", "sbyte", "sealed", "short", "sizeof",
            "stackalloc", "static", "string", "select", "this",
            "throw", "true", "try", "typeof", "uint", "ulong", "unchecked",
            "unsafe", "ushort", "using", "var", "virtual", "volatile",
            "void", "while", "where", "yield"};

    String[] separator = {";", "{", "}", "\r", "\n", "\r\n"};

    String[] comments = {"//", "/*", "*/"};

    String[] operators = {"+", "-", "*", "/", "%", "&", "(", ")", "[", "]",
            "|", "^", "!", "~", "&&", "||", ",",
            "++", "--", "<<", ">>", "==", "!=", "<", ">", "<=",
            ">=", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=",
            "^=", "<<=", ">>=", ".", "[]", "()", "?:", "=>", "??"};

    public String Parse(String item) {

        StringBuilder str = new StringBuilder();

        try {
            int x = Integer.parseInt(item);
            str.append("(numerical constant, " + item + ") ");
            return str.toString();
        } catch (NumberFormatException e) {
        }

        if (item.equals("\r\n")) {
            return "\r\n";
        }

        if (CheckKeyword(item)) {
            str.append("(keyword, " + item + ") ");
            return str.toString();
        }

        if (CheckOperator(item)) {
            str.append("(operator, " + item + ") ");
            return str.toString();
        }
        if (CheckDelimiter(item)) {
            str.append("(separator, " + item + ") ");
            return str.toString();
        }

        str.append("(identifier, " + item + ") ");
        return str.toString();
    }


    private boolean CheckOperator(String str) {
        if (Arrays.asList(operators).contains(str))
            return true;
        return false;
    }

    private boolean CheckDelimiter(String str) {
        if (Arrays.asList(separator).contains(str))
            return true;
        return false;
    }

    private boolean CheckKeyword(String str) {
        if (Arrays.asList(keywords).contains(str))
            return true;
        return false;
    }

    private boolean CheckComments(String str) {
        if (Arrays.asList(comments).contains(str))
            return true;
        return false;
    }

    public String[] GetNextLexicalAtom(String item) {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < item.length(); i++) {
            if (CheckDelimiter(Character.toString(item.charAt(i))))
            {
                if (i + 1 < item.length() && CheckDelimiter(StringUtils.substring(item, i, i + 2))) {
                    token.append(item.substring(i, i + 2));
                    item = item.substring(0, i) + item.substring(i + 2);

                    return new String[]{Parse(token.toString()), item};
                } else {
                    token.append(Character.toString(item.charAt(i)));
                    item = item = item.substring(0, i) + item.substring(i + 1);
                    return new String[]{Parse(token.toString()), item};
                }

            } else if (CheckOperator(Character.toString(item.charAt(i))))
            {
                if (i + 1 < item.length() && (CheckOperator(item.substring(i, i + 2))))
                    if (i + 2 < item.length() && CheckOperator(item.substring(i, i + 3))) {
                        token.append(item.substring(i, i + 3));
                        item = item.substring(0, i) + item.substring(i + 3);
                        return new String[]{Parse(token.toString()), item};
                    } else {
                        token.append(item.substring(i, i + 2));
                        item = item.substring(0, i) + item.substring(i + 2);
                        return new String[]{Parse(token.toString()), item};
                    }
                else if (CheckComments(item.substring(i, i + 2))) {
                    if (item.substring(i, i + 2).equals("//")) {
                        do {
                            i++;
                        }
                        while (item.charAt(i) != '\n');
                        item = item.substring(i + 1);
                        item = StringUtils.strip(item, " \t\r\n");
                        i = -1;
                    } else {
                        do {
                            i++;
                        } while (item.substring(i, i + 2).equals("*/") == false);
                        item = item.substring(i + 2);
                        item = StringUtils.strip(item, " \t\r\n");
                        i = -1;
                    }

                } else {
                    try {
                        if (item.charAt(i) == '-') {
                            int x = Integer.parseInt(Character.toString(item.charAt(i + 1)));
                            continue;
                        }
                    } catch (NumberFormatException e) {
                    }
                    token.append(item.charAt(i));
                    item = item.substring(0, i) + item.substring(i + 1);
                    return new String[]{Parse(token.toString()), item};
                }

            } else if (item.charAt(i) == '\'')
            {
                int j = i + 1;
                if (item.charAt(j) == '\\')
                    j += 2;
                else
                    j++;

                token.append("(literal constant, ").append(item.substring(i, i + j - i + 1)).append(") ");
                item = item.substring(0, i) + item.substring(i + j - i + 1);
                return new String[]{token.toString(), item};
            } else if (item.charAt(i) == '"')
            {
                int j = i + 1;
                while (item.charAt(j) != '"')
                    j++;
                token.append("(literal constant, ").append(item.substring(i, i + j - i + 1)).append(") ");
                item = item.substring(0, i) + item.substring(i + j - i + 1);
                return new String[]{token.toString(), item};
            } else if (Character.toString(item.charAt(i + 1)).equals(" ") || CheckDelimiter(Character.toString(item.charAt(i + 1))) == true
                    || CheckOperator(Character.toString(item.charAt(i + 1))) == true)
            {
                if (Parse(item.substring(0, i + 1)).contains("numerical constant") && item.charAt(i + 1) == '.')
                {
                    int j = i + 2;
                    while ((Character.toString(item.charAt(j)).equals(" ")) == false &&
                            CheckDelimiter(Character.toString(item.charAt(j))) == false &&
                            CheckOperator(Character.toString(item.charAt(j))) == false)
                        j++;
                    try {
                        int x = Integer.parseInt(item.substring(i + 2, i + 2 + j - i - 2));
                        token.append("(numerical constant, ").append(item.substring(0, j)).append(") ");
                        item = item.substring(j);
                        return new String[]{token.toString(), item};
                    } catch (NumberFormatException e) {
                    }
                }
                token.append(item.substring(0, i + 1));
                item = item.substring(i + 1);
                return new String[]{Parse(token.toString()), item};
            }

        }
        return new String[]{null, null};
    }
}