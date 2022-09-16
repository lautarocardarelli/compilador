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

    @Disabled
    public void assignmentWithExpression() throws Exception {
        compilationSuccessful("c=d*(e-21)/4");
    }

    @Disabled
    public void syntaxError() {
        compilationError("1234");
    }

    @Disabled
    void assignments() throws Exception {
        compilationSuccessful(readFromFile("assignments.txt"));
    }

    @Disabled
    void write() throws Exception {
        compilationSuccessful(readFromFile("write.txt"));
    }

    @Disabled
    void read() throws Exception {
        compilationSuccessful(readFromFile("read.txt"));
    }

    @Disabled
    void comment() throws Exception {
        compilationSuccessful(readFromFile("comment.txt"));
    }

    @Disabled
    void init() throws Exception {
        compilationSuccessful(readFromFile("init.txt"));
    }

    @Disabled
    void and() throws Exception {
        compilationSuccessful(readFromFile("and.txt"));
    }

    @Disabled
    void or() throws Exception {
        compilationSuccessful(readFromFile("or.txt"));
    }

    @Disabled
    void not() throws Exception {
        compilationSuccessful(readFromFile("not.txt"));
    }

    @Disabled
    void ifStatement() throws Exception {
        compilationSuccessful(readFromFile("if.txt"));
    }

    @Disabled
    void whileStatement() throws Exception {
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
