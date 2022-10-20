package lyc.compiler.model;
import lyc.compiler.model.Simbolo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TablaDeSimbolos {
    Set<Simbolo> simbolos = new HashSet<Simbolo>();

    public void save(String tipo, String valor) {
        String nombre = "_" + valor;
        int longitud = -1;
        if (tipo.compareTo("ID") == 0) longitud = valor.length();

        simbolos.add(new Simbolo(nombre, tipo, valor, longitud));
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
            bw.write("Nombre "+ "\t  "+ "Tipo "+ "\t  "+ "valor " + " \t   "+ "Longitud "+ "\n");
            this.simbolos.forEach(simbolo -> {
                try {
                    bw.write(simbolo.getNombre() + "\t " + simbolo.getTipo() + "\t " + simbolo.getValor() + "\t " + simbolo.getLongitud() +"\n");
                }
                catch (Exception ex) {

                }

            });
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}