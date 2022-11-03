package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class IntermediateCodeGenerator implements FileGenerator {

    private static Integer celdaActual = 0;
    private static Stack<Integer> cellStack = new Stack<>();

    private static Stack<String> valueStack = new Stack<>();
    private static List<String> polaca = new ArrayList<String>();

    public void insertarEnPolaca(Object o) {
        polaca.add(o.toString());
        celdaActual++;
    }

    public void insertarEnPolaca(Object... objectArray) {
        for (Object o: objectArray) {
            polaca.add(o.toString());
            celdaActual++;
        }
    }
    public void apilarCeldaActual() {
        cellStack.push(polaca.size());
    }
    public void apilarCeldaActualYAvanzar() {
        cellStack.push(polaca.size());
        celdaActual++;
    }

    public Integer obtenerTopePilaCelda(){
        return cellStack.pop();
    }

    public void apilar(String value) {
        valueStack.push(value);
    }
    public void apilarAvanzar(String value){
        valueStack.push(value);
        celdaActual++;
    }

    public String obtenerTopePilaValue(){
        return valueStack.pop();
    }

    public void mostrarPolaca() {
        polaca.forEach((p) -> System.out.println(p));
    }
    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("TODO");
    }
}
