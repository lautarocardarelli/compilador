package lyc.compiler.files;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

public class AsmCodeGenerator implements FileGenerator {

    IntermediateCodeGenerator icg = IntermediateCodeGenerator.getIcgInstance();
    SymbolTableGenerator stg = SymbolTableGenerator.getStgInstance();
    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        fileWriter.write("include macros2.asm\n");
        fileWriter.write("include number.asm\n");
        fileWriter.write("\n");

        fileWriter.write(".MODEL \t LARGE\n");
        fileWriter.write(".386 \n");
        fileWriter.write(".STACK 200h \n");

        fileWriter.write("\n");
        fileWriter.write(".DATA\n");

        Formatter fmt = new Formatter();
        stg.simbolos.forEach(simbolo -> {
            String valor;
            if (simbolo.tipo == "ID") {
                valor = "?";
            } else {
                valor = simbolo.getValor();
            }
            fmt.format("%15s %15s %15s\n", simbolo.getNombre(), "dd", valor);
        });

        fileWriter.write(String.valueOf(fmt));
        fileWriter.write(".CODE\n");
    }
}
