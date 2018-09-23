import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

class LexicalAnalyzer {

    private String input = "";
    private String inputLineSeparator = "";

    private void setInputLineSeparator(String input) {
        inputLineSeparator = Utils.getInputLineSeparator(input);
    }

    private LexicalAnalyzer() {
    }

    public LexicalAnalyzer(String input) {
        // removing BOM to correct work with it
        input = Utils.removeUTF8BOM(input);
        // supporting different line separator types
        setInputLineSeparator(input);
        this.input = input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

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
    public String GetNextLexicalAtom(String input) {

        // trim tabulation
        input = StringUtils.strip(input, " \t");

        StringBuilder token = new StringBuilder();

        // check all element
        for (int i = 0; i < input.length(); i++) {

            // if it is delimiter:
            if (CheckDelimiter(Character.toString(input.charAt(i)))) {
                // two symbols delimiter
                if (i + 1 < input.length() && CheckDelimiter(StringUtils.substring(input, i, i + 2))) {
                    token.append(input.substring(i, i + 2));
                    input = input.substring(0, i) + input.substring(i + 2);

                    setInput(input);
                    return Parse(token.toString());
                }
                // one symbol delimiter
                else {
                    token.append(Character.toString(input.charAt(i)));
                    input = input = input.substring(0, i) + input.substring(i + 1);

                    setInput(input);
                    return Parse(token.toString());
                }

            }
            // check if it is an operator
            else if (CheckOperator(Character.toString(input.charAt(i)))) {
                if (i + 1 < input.length() && (CheckOperator(input.substring(i, i + 2))))
                    // 3 symbol operators
                    if (i + 2 < input.length() && CheckOperator(input.substring(i, i + 3))) {
                        token.append(input.substring(i, i + 3));
                        input = input.substring(0, i) + input.substring(i + 3);

                        setInput(input);
                        return Parse(token.toString());
                    }
                    // 2 symbol operators
                    else {
                        token.append(input.substring(i, i + 2));
                        input = input.substring(0, i) + input.substring(i + 2);

                        setInput(input);
                        return Parse(token.toString());
                    }
                    // if we cannot gather 2 or 3 symbol operators -> check comments
                else if (CheckComments(input.substring(i, i + 2))) {
                    // one line comments
                    if (input.substring(i, i + 2).equals("//")) {
                        do {
                            i++;
                        }
                        while (i < input.length() && input.charAt(i) != '\n');
                        if (i >= input.length()) {
                            setInput(null);
                            return "";
                        }
                        input = input.substring(i + 1);
                        input = StringUtils.strip(input, " \t\r\n");
                        i = -1;
                    }
                    // otherwise multiline comments
                    else {
                        // find last element and ignore all
                        do {
                            i++;
                        } while (input.substring(i, i + 2).equals("*/") == false);
                        input = input.substring(i + 2);
                        input = StringUtils.strip(input, " \t\r\n");
                        i = -1;
                    }

                } else {

                    // check negative number
                    if (input.charAt(i) == '-') {
                        if (Utils.isInteger(Character.toString(input.charAt(i + 1)))) {
                            continue;
                        }
                    }

                    // otherwise this is 1(one) symbol operators
                    token.append(input.charAt(i));
                    input = input.substring(0, i) + input.substring(i + 1);

                    setInput(input);
                    return Parse(token.toString());
                }
            } // if current symbol is ''' ->
            else if (input.charAt(i) == '\'') {
                int j = i + 1;
                if (input.charAt(j) == '\\')
                    j += 2;
                else
                    j++;

                token.append("[Literal constant -> ").append(input.substring(i, j + 1)).append("] ");
                input = input.substring(0, i) + input.substring(j + 1);

                setInput(input);
                return token.toString();
            }
            // other literal constant
            else if (input.charAt(i) == '"') {
                int j = i + 1;
                while (input.charAt(j) != '"')
                    j++;
                token.append("[Literal constant -> ").append(input.substring(i, j + 1)).append("] ");
                input = input.substring(0, i) + input.substring(j + 1);

                setInput(input);
                return token.toString();
            } // if it is delimiter or operator:
            else if (Character.toString(input.charAt(i + 1)).equals(" ") ||
                    CheckDelimiter(Character.toString(input.charAt(i + 1)))
                    || CheckOperator(Character.toString(input.charAt(i + 1)))) {
                // try to find numerical constant
                if (Parse(input.substring(0, i + 1)).contains("Numerical constant") && input.charAt(i + 1) == '.') {
                    int j = i + 2;

                    // find right bound
                    while (!(Character.toString(input.charAt(j)).equals(" ")) &&
                            !CheckDelimiter(Character.toString(input.charAt(j))) &&
                            !CheckOperator(Character.toString(input.charAt(j))))
                        j++;

                    // test that this input substring is correct integer
                    if (Utils.isInteger(input.substring(i + 2, j))) {
                        token.append("[Numerical constant -> \"").append(input.substring(0, j)).append("\"] ");
                        input = input.substring(j);

                        setInput(input);
                        return token.toString();
                    }
                }
                token.append(input.substring(0, i + 1));
                input = input.substring(i + 1);

                setInput(input);
                return Parse(token.toString());
            }

        }
        // of there is no token -> return empty token

        setInput(null);
        return "";
    }

    private String Parse(String input) {

        StringBuilder str = new StringBuilder();

        // if it is an integer:
        if (Utils.isInteger(input)) {
            // it is numerical constant
            str.append("[Numerical constant -> \"" + input + "\"] ");
            return str.toString();
        }

        // new line
        if (input.equals(inputLineSeparator)) {
            return inputLineSeparator;
        }

        // if it is a keyword:
        if (CheckKeyword(input)) {
            str.append("[Keyword -> \"" + input + "\"] ");
            return str.toString();
        }

        // if it is an operator:
        if (CheckOperator(input)) {
            str.append("[Operator -> \"" + input + "\"] ");
            return str.toString();
        }

        // if it is a delimiter
        if (CheckDelimiter(input)) {
            str.append("[Delimiter -> \"" + input + "\"] ");
            return str.toString();
        }

        // else it is identifer
        str.append("[Identifier -> \"" + input + "\"] ");
        return str.toString();
    }
}
