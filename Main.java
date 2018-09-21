import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

public class Main {
    public static final String UTF8_BOM = "\uFEFF";

    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
    public static void main(String[] args) throws IOException {
        File file = new File("Program.cs");
        String text = FileUtils.readFileToString(file);
        text = removeUTF8BOM(text);

        LexicalAnalyzer analyzer = new LexicalAnalyzer();

        while (text != null) {
            text = StringUtils.strip(text, " \t");
            String[] res = analyzer.GetNextLexicalAtom(text);
            text = res[1];
            if (res[1] != null)
                System.out.print(res[0]);
        }
    }
}
