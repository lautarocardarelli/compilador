package lyc.compiler.files;
import lyc.compiler.model.Simbolo;

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

    public boolean isTokenSaved(String name) {
        boolean result = false;
        for (Simbolo simbolo : simbolos) {
            if (simbolo.nombre.equals(name)) {
                result = true;
            }
        }
        return result;
    }

    public String getToken(String name) {
        String result = null;
        for (Simbolo simbolo : simbolos) {
            if (simbolo.nombre.equals(name)) {
                result = simbolo.getNombre();
            }
        }
        return result;
    }

    public void updateIdType(String id, String type) {
        for (Simbolo simbolo : simbolos) {
            if (simbolo.nombre.equals(id)) {
                simbolo.tipo = type;
            }
        }
    }
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