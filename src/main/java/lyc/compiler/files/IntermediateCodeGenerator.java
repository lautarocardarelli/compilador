package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IntermediateCodeGenerator implements FileGenerator {

    private static Integer celdaActual = 0;
    private static Stack<Integer> stack = new Stack<>();
    private static List<String> polaca = new ArrayList<String>();

    public void insertarEnPolaca(Object o) {
        polaca.add(o.toString());
        celdaActual++;
    }

    public void apilarCeldaActual() {
        stack.push(polaca.size());
    }
    public void apilarAvanzar(){
        stack.push(polaca.size());
        celdaActual++;
    }

    public Integer obtenerTopePila(){
        return stack.pop();
    }

    public void mostrarPolaca() {
        polaca.forEach((p) -> System.out.println(p));
    }
    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("TODO");
    }
}
