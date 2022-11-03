package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.factories.ParserFactory;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    @Test
    public void allEqual() throws Exception {
        System.out.println("[Test] - all equal -----------------");
        compilationSuccessful(readFromFile("allequal.txt"));
    }
    @Test
    public void assignmentWithExpression() throws Exception {
        System.out.println("[Test] - assignmentWithExpression -----------------");
        compilationSuccessful("c=d*(e - 21)/4");
    }

    @Test
    public void syntaxError() {
        System.out.println("[Test] - syntaxError -----------------");
        compilationError("1234");
    }

    @Test
    void assignments() throws Exception {
        System.out.println("[Test] - assignments -----------------");
        compilationSuccessful(readFromFile("assignments.txt"));
    }

    @Test
    void write() throws Exception {
        System.out.println("[Test] - write -----------------");
        compilationSuccessful(readFromFile("write.txt"));
    }

    @Test
    void read() throws Exception {
        System.out.println("[Test] - read -----------------");
        compilationSuccessful(readFromFile("read.txt"));
    }

    @Test
    void comment() throws Exception {
        System.out.println("[Test] - comment -----------------");
        compilationSuccessful(readFromFile("comment.txt"));
    }

    @Test
    void init() throws Exception {
        compilationSuccessful(readFromFile("init.txt"));
    }

    @Test
    void and() throws Exception {
        System.out.println("[Test] - and -----------------");
        compilationSuccessful(readFromFile("and.txt"));
    }

    @Test
    void or() throws Exception {
        System.out.println("[Test] - or -----------------");
        compilationSuccessful(readFromFile("or.txt"));
    }

    @Test
    void not() throws Exception {
        System.out.println("[Test] - not -----------------");
        compilationSuccessful(readFromFile("not.txt"));
    }

    @Test
    void ifStatement() throws Exception {
        System.out.println("[Test] - ifstatement -----------------");
        compilationSuccessful(readFromFile("if.txt"));
    }

    @Test
    void whileStatement() throws Exception {
        System.out.println("[Test] - whilestatment -----------------");
        compilationSuccessful(readFromFile("while.txt"));
    }
    private void compilationSuccessful(String input) throws Exception {
        assertThat(scan(input).sym).isEqualTo(ParserSym.EOF);
    }

    private void compilationError(String input){
        assertThrows(Exception.class, () -> scan(input));
    }

    private Symbol scan(String input) throws Exception {
        return ParserFactory.create(input).parse();
    }

    private String readFromFile(String fileName) throws IOException {
        InputStream resource = getClass().getResourceAsStream("/%s".formatted(fileName));
        assertThat(resource).isNotNull();
        return IOUtils.toString(resource, StandardCharsets.UTF_8);
    }


}
