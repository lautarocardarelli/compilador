package lyc.compiler.files;
import lyc.compiler.model.Simbolo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

public class SymbolTableGenerator implements FileGenerator {
    /* Singleton */
    private static SymbolTableGenerator stg;
    private SymbolTableGenerator() {

    }
    public static SymbolTableGenerator getStgInstance() {
        if (stg == null) {
            stg = new SymbolTableGenerator();
        }
        return stg;
    }
    /* Fin Singleton */

    Set<Simbolo> simbolos = new HashSet<Simbolo>();

    /*    public String getToken(String token) throws TokenNotFound {
            String result;
            this.simbolos.forEach(simbolo -> {
                if (simbolo.nombre == token) {
                    result = token;
                }
            });
        }*/
    public void save(String tipo, String valor) {
        switch (tipo) {
            case "ID":
                simbolos.add(new Simbolo(valor, tipo));
                break;
            case "CTE_STRING":
                simbolos.add(new Simbolo("_" + valor, tipo, valor, Integer.toString(valor.length() - 2)));
                break;
            case "CTE_INTEGER":
            case "CTE_FLOAT":
                simbolos.add(new Simbolo("_" + valor, tipo, valor));
                break;
            default:
                break;
        }
    }

    @Override
    public void generate(FileWriter fileWriter) throws IOException {
        try {
            Formatter fmt = new Formatter();
            fmt.format("%15s %15s %15s %15s\n", "Nombre", "Tipo", "Valor", "Longitud");
            simbolos.forEach(simbolo -> {
                try {
                    fmt.format("%15s %15s %15s %15s\n", simbolo.getNombre(), simbolo.getTipo(), simbolo.getValor(), simbolo.getLongitud());
                }
                catch (Exception ex) {

                }

            });
            fileWriter.write(String.valueOf(fmt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}