import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class LexicalAnalyzerTest {

    private LexicalAnalyzer lexicalAnalyzer;

    @Test
    public void testCheckOperator1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckOperator", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, ">>=");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckOperator2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckOperator", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, ">>|");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckKeyword1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckKeyword", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "string");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckKeyword2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckKeyword", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "String");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckDelimiter1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckDelimiter", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "\r\n");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckDelimiter2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckDelimiter", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "{");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckDelimiter3() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckDelimiter", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "{{");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckDelimiter4() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckDelimiter", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "\n\r");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckComments1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckComments", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "/*");
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckComments2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("CheckComments", String.class);
        method.setAccessible(true);
        boolean actual = (Boolean) method.invoke(lexicalAnalyzer, "///");
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void testParse1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("Parse", String.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(lexicalAnalyzer, "PETR_AND_ANSAT_ARE_AWESOME_TEAM");
        String expected = "[Identifier -> \"PETR_AND_ANSAT_ARE_AWESOME_TEAM\"] ";
        assertEquals(expected, actual);
    }

    @Test
    public void testParse2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("Parse", String.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(lexicalAnalyzer, "241441241");
        String expected = "[Numerical Constant -> \"" + 241441241 + "\"] ";
        assertEquals(expected, actual);
    }

    @Test
    public void testParse3() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("Parse", String.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(lexicalAnalyzer, "using");
        String expected = "[Keyword -> \"using\"] ";
        assertEquals(expected, actual);
    }


    @Test
    public void testParse4() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("Parse", String.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(lexicalAnalyzer, "=>");
        String expected = "[Operator -> \"=>\"] ";
        assertEquals(expected, actual);
    }

    @Test
    public void testParse5() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        lexicalAnalyzer = new LexicalAnalyzer();
        Method method = lexicalAnalyzer.getClass().getDeclaredMethod("Parse", String.class);
        method.setAccessible(true);
        String actual = (String) method.invoke(lexicalAnalyzer, ";");
        String expected = "[Delimiter -> \";\"] ";
        assertEquals(expected, actual);
    }

}