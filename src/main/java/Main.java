import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws IOException {
        // ok, let's start

        // use same input and output file names as previous homework
        File file = new File("in.txt");
        PrintWriter out = new PrintWriter(new File("out.txt"));

        // read all file
        String input = FileUtils.readFileToString(file);

        LexicalAnalyzer analyzer = new LexicalAnalyzer(input);

        // while we have lexical atoms:
        while ((input = analyzer.getInput()) != null) {
            String nextToken = analyzer.GetNextLexicalAtom(input);
            out.print(nextToken);
        }
        out.close();
    }
}
