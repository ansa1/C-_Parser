import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

class LexicalAnalyzer {

    // delimiter class
    private final static String[] delimiter = {";", "{", "}", "\r", "\n", "\r\n"};

    // check that this 'str' is delimiter
    private boolean CheckDelimiter(String str) {
        return Arrays.asList(delimiter).contains(str);
    }

    // operator class
    private final static String[] operators = {"%=", "==", "??", "+", "*=", "[]", "<", "!", "%", "~", "++", "(", ",",
            "=>", ">=", ")", "&", "||", "/=", "<<=", "|=", "&&", "*", "-=",
            "^=", "<<", ">>", "[", "<=", "=", "^", "/", "-", "?:", "--",
            "()", ">>=", "&=", ".", "]", ">", "+=", "!=", "|"};

    // check that this 'str' is operator
    private boolean CheckOperator(String str) {
        return Arrays.asList(operators).contains(str);
    }

    // comment class, of course we will ignore it
    private final static String[] comments = {"//", "/*", "*/"};

    // check that this 'str' is comment
    private boolean CheckComments(String str) {
        return Arrays.asList(comments).contains(str);
    }

    // keyword class
    private final static String[] keywords = {"abstract", "as", "base", "bool", "break", "by",
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

    // check that this 'str' is keyword
    private boolean CheckKeyword(String str) {
        return Arrays.asList(keywords).contains(str);
    }

    // this method finds next lexical atom
    public String[] GetNextLexicalAtom(String item) {
        StringBuilder token = new StringBuilder();

        // check all element
        for (int i = 0; i < item.length(); i++) {

            // if it is delimiter:
            if (CheckDelimiter(Character.toString(item.charAt(i)))) {
                // two symbols delimiter
                if (i + 1 < item.length() && CheckDelimiter(StringUtils.substring(item, i, i + 2))) {
                    token.append(item.substring(i, i + 2));
                    item = item.substring(0, i) + item.substring(i + 2);

                    return new String[]{Parse(token.toString()), item};
                }
                // one symbol delimiter
                else {
                    token.append(Character.toString(item.charAt(i)));
                    item = item = item.substring(0, i) + item.substring(i + 1);
                    return new String[]{Parse(token.toString()), item};
                }

            }
            // check if it is an operator
            else if (CheckOperator(Character.toString(item.charAt(i)))) {
                if (i + 1 < item.length() && (CheckOperator(item.substring(i, i + 2))))
                    // 3 symbol operators
                    if (i + 2 < item.length() && CheckOperator(item.substring(i, i + 3))) {
                        token.append(item.substring(i, i + 3));
                        item = item.substring(0, i) + item.substring(i + 3);
                        return new String[]{Parse(token.toString()), item};
                    }
                    // 2 symbol operators
                    else {
                        token.append(item.substring(i, i + 2));
                        item = item.substring(0, i) + item.substring(i + 2);
                        return new String[]{Parse(token.toString()), item};
                    }
                    // if we cannot gather 2 or 3 symbol operators -> check comments
                else if (CheckComments(item.substring(i, i + 2))) {
                    // one line comments
                    if (item.substring(i, i + 2).equals("//")) {
                        do {
                            i++;
                        }
                        while (item.charAt(i) != '\n');
                        item = item.substring(i + 1);
                        item = StringUtils.strip(item, " \t\r\n");
                        i = -1;
                    }
                    // otherwise multiline comments
                    else {
                        // find last element and ignore all
                        do {
                            i++;
                        } while (item.substring(i, i + 2).equals("*/") == false);
                        item = item.substring(i + 2);
                        item = StringUtils.strip(item, " \t\r\n");
                        i = -1;
                    }

                } else {

                    // check negative number
                    try {
                        if (item.charAt(i) == '-') {
                            int x = Integer.parseInt(Character.toString(item.charAt(i + 1)));
                            continue;
                        }
                    } catch (NumberFormatException e) {
                    }

                    // otherwise this is 1(one) symbol operators
                    token.append(item.charAt(i));
                    item = item.substring(0, i) + item.substring(i + 1);
                    return new String[]{Parse(token.toString()), item};
                }
            } // if current symbol is ''' ->
            else if (item.charAt(i) == '\'') {
                int j = i + 1;
                if (item.charAt(j) == '\\')
                    j += 2;
                else
                    j++;

                token.append("[Literal constant -> ").append(item.substring(i, i + j - i + 1)).append("] ");
                item = item.substring(0, i) + item.substring(i + j - i + 1);
                return new String[]{token.toString(), item};
            }
            // other literal constant
            else if (item.charAt(i) == '"') {
                int j = i + 1;
                while (item.charAt(j) != '"')
                    j++;
                token.append("[Literal constant -> ").append(item.substring(i, i + j - i + 1)).append("] ");
                item = item.substring(0, i) + item.substring(i + j - i + 1);
                return new String[]{token.toString(), item};
            } // if it is delimiter or operator:
            else if (Character.toString(item.charAt(i + 1)).equals(" ") ||
                    CheckDelimiter(Character.toString(item.charAt(i + 1)))
                    || CheckOperator(Character.toString(item.charAt(i + 1)))) {
                // try to find numerical constant
                if (Parse(item.substring(0, i + 1)).contains("Numerical constant") && item.charAt(i + 1) == '.') {
                    int j = i + 2;

                    // find right bound
                    while (!(Character.toString(item.charAt(j)).equals(" ")) &&
                            !CheckDelimiter(Character.toString(item.charAt(j))) &&
                            !CheckOperator(Character.toString(item.charAt(j))))
                        j++;

                    // test that this item substring is corrent integer
                    try {
                        int x = Integer.parseInt(item.substring(i + 2, i + 2 + j - i - 2));
                        token.append("[Numerical constant -> \"").append(item.substring(0, j)).append("\"] ");
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
        // of there is no token -> return empty token
        return new String[]{null, null};
    }

    private String Parse(String item) {

        StringBuilder str = new StringBuilder();

        // if it is an integer:
        try {
            int x = Integer.parseInt(item);

            // it is numerical constant
            str.append("[Numerical Constant -> \"" + item + "\"] ");
            return str.toString();
        } catch (NumberFormatException e) {
        }

        // new line
        if (item.equals("\r\n")) {
            return "\r\n";
        }

        // if it is a keyword:
        if (CheckKeyword(item)) {
            str.append("[Keyword -> \"" + item + "\"] ");
            return str.toString();
        }

        // if it is an operator:
        if (CheckOperator(item)) {
            str.append("[Operator -> \"" + item + "\"] ");
            return str.toString();
        }

        // if it is a delimiter
        if (CheckDelimiter(item)) {
            str.append("[Delimiter -> \"" + item + "\"] ");
            return str.toString();
        }

        // else it is identifer
        str.append("[Identifier -> \"" + item + "\"] ");
        return str.toString();
    }
}
