import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    // constant string to remove BOM from the file
    public static final String UTF8_BOM = "\uFEFF";

    // removing BOM method
    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    public static void main(String[] args) throws IOException {
        // ok, let's start

        // use same input and output file names as previous homework
        File file = new File("in.txt");
        PrintWriter out = new PrintWriter(new File("out.txt"));

        // read all file
        String text = FileUtils.readFileToString(file);
        // removing BOM to correct work with it
        text = removeUTF8BOM(text);

        LexicalAnalyzer analyzer = new LexicalAnalyzer(text);
        // while we have lexical atoms:
        while (text != null) {

            // trim tabulation
            text = StringUtils.strip(text, " \t");
            String nextToken = analyzer.GetNextLexicalAtom(text);
            text = analyzer.getItem();
            if (text != null) out.print(nextToken);
        }
        out.close();
    }
}
