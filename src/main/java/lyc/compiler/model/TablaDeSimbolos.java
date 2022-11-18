package lyc.compiler.model;
import lyc.compiler.model.Simbolo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Formatter;

public class TablaDeSimbolos {
    Set<Simbolo> simbolos = new HashSet<Simbolo>();


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
    public void generate() throws IOException {
        try {
            String ruta = "./target/symbol-table.txt";
            File file = new File(ruta);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            Formatter fmt = new Formatter();
            fmt.format("%15s %15s %15s %15s\n", "Nombre", "Tipo", "Valor", "Longitud");
            this.simbolos.forEach(simbolo -> {
                try {
                    fmt.format("%15s %15s %15s %15s\n", simbolo.getNombre(), simbolo.getTipo(), simbolo.getValor(), simbolo.getLongitud());
                }
                catch (Exception ex) {

                }

            });
            bw.write(String.valueOf(fmt));
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}